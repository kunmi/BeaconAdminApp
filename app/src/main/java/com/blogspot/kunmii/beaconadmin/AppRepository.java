package com.blogspot.kunmii.beaconadmin;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.blogspot.kunmii.beaconadmin.Helpers.Helpers;
import com.blogspot.kunmii.beaconadmin.data.Beacon;
import com.blogspot.kunmii.beaconadmin.data.BeaconDAO;
import com.blogspot.kunmii.beaconadmin.data.FloorPlan;
import com.blogspot.kunmii.beaconadmin.data.FloorplanDAO;
import com.blogspot.kunmii.beaconadmin.data.FloorplanWithBeacons;
import com.blogspot.kunmii.beaconadmin.data.Project;
import com.blogspot.kunmii.beaconadmin.data.ProjectDAO;
import com.blogspot.kunmii.beaconadmin.network.IServerRequestListener;
import com.blogspot.kunmii.beaconadmin.network.ServerRequest;
import com.blogspot.kunmii.beaconadmin.network.ServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AppRepository {

    Application mContext;
    AppDatabase db;

    ProjectDAO projecDao;
    FloorplanDAO floorplanDao;
    BeaconDAO beaconDAO;

    LiveData<List<Project>> projects;

    public AppRepository(Application application)
    {
        mContext = application;
        db = AppDatabase.getInstance(mContext);

        projecDao = db.projectDAO();
        floorplanDao = db.floorplanDAO();
        beaconDAO = db.beaconDAO();

        projects = projecDao.getAll();
    }

    public LiveData<List<Project>> getAssignedProjects(){
        checkForUpdates();
        return projects;
    }

    public LiveData<List<FloorplanWithBeacons>> getFloorplanForProject(String objectId) {
        return floorplanDao.loadFloorPlansWithBeaconsForProjects(objectId);
    }

    public void checkForUpdates(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                List<Project> projs = projecDao.getProjectsRaw();

                if(projs == null || projs.size()==0)
                {
                    ServerRequest request = Helpers.craftProjectRetrieveRequest(mContext);
                    request.execute(new IServerRequestListener() {
                        @Override
                        public void onResponse(ServerResponse response) {
                            String res  = response.getJsonBody();

                            try {
                                JSONArray array = new JSONArray(res);
                                for(int i=0; i< array.length(); i++)
                                {
                                    JSONObject jsonObject = array.getJSONObject(i);

                                    Project project = new Project();
                                    project.setObjectId(jsonObject.getString(Config.NETWORK_JSON_NODE.OBJECT_ID));
                                    project.setDescription(jsonObject.getString(Config.NETWORK_JSON_NODE.PROJECT_DESCRIPTION));
                                    project.setEmail(jsonObject.getString(Config.NETWORK_JSON_NODE.PROJECT_EMAIL));
                                    project.setName(jsonObject.getString(Config.NETWORK_JSON_NODE.PROJECT_NAME));

                                    if(jsonObject.has(Config.NETWORK_JSON_NODE.UPDATED))
                                    {

                                        project.setUpdated(jsonObject.getString(Config.NETWORK_JSON_NODE.UPDATED));
                                    }
                                    else
                                    {
                                        project.setUpdated(jsonObject.getString(Config.NETWORK_JSON_NODE.CREATED));
                                    }


                                    JSONArray floorplanArray = jsonObject.getJSONArray(Config.NETWORK_JSON_NODE.PROJECT_FLOORPLANS);
                                    for(int j=0; j< floorplanArray.length(); j++)
                                    {
                                        JSONObject jsonFloorplan = floorplanArray.getJSONObject(i);

                                        FloorPlan floorPlan = new FloorPlan();
                                        floorPlan.setProjectObjectId(project.getObjectId());
                                        floorPlan.setName(jsonFloorplan.getString(Config.NETWORK_JSON_NODE.FLOORPLAN_NAME));
                                        floorPlan.setObjectId(jsonFloorplan.getString(Config.NETWORK_JSON_NODE.OBJECT_ID));
                                        floorPlan.setFileurl(jsonFloorplan.getString(Config.NETWORK_JSON_NODE.FLOORPLAN_URL));

                                        if(jsonFloorplan.has(Config.NETWORK_JSON_NODE.UPDATED))
                                        {
                                            floorPlan.setUpdated(jsonFloorplan.getString(Config.NETWORK_JSON_NODE.UPDATED));
                                        }
                                        else
                                        {
                                            floorPlan.setUpdated(jsonFloorplan.getString(Config.NETWORK_JSON_NODE.CREATED));
                                        }

                                        JSONArray beaconArray = jsonFloorplan.getJSONArray(Config.NETWORK_JSON_NODE.FLOORPLAN_BEACONS);
                                        for(int k=0; k<beaconArray.length(); k++)
                                        {
                                            JSONObject beaconJson = beaconArray.getJSONObject(k);
                                            Beacon beacon = new Beacon();

                                            beacon.setFloorPlanId(floorPlan.getObjectId());
                                            beacon.setProjectId(project.getObjectId());
                                            beacon.setObjectId(beaconJson.getString(Config.NETWORK_JSON_NODE.OBJECT_ID));
                                            beacon.setMap(beaconJson.getJSONObject(Config.NETWORK_JSON_NODE.BEACON_MAP).toString());
                                            beacon.setType(beaconJson.getString(Config.NETWORK_JSON_NODE.BEACON_TYPE));

                                            beacon.setRef(beaconJson.getString(Config.NETWORK_JSON_NODE.BEACON_REF));
                                            beacon.setTxpower(beaconJson.getString(Config.NETWORK_JSON_NODE.BEACON_TXPOWER));

                                            if(beaconJson.has(Config.NETWORK_JSON_NODE.UPDATED))
                                            {
                                                beacon.setUpdated(beaconJson.getString(Config.NETWORK_JSON_NODE.UPDATED));
                                            }
                                            else
                                            {
                                                beacon.setUpdated(beaconJson.getString(Config.NETWORK_JSON_NODE.CREATED));
                                            }

                                            beaconDAO.insertBeacon(beacon);
                                        }

                                        floorplanDao.insert(floorPlan);
                                    }
                                    projecDao.insertAll(project);
                                }
                            }
                            catch (JSONException exp)
                            {
                                exp.printStackTrace();
                            }
                        }
                    });
                }


            }
        }).start();
    }
}
