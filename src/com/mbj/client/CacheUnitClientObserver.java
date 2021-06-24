package com.mbj.client;

import com.mbj.view.CacheUnitView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

public class CacheUnitClientObserver implements PropertyChangeListener {

    private final CacheUnitClient cacheUnitClient;

    public CacheUnitClientObserver(){
        cacheUnitClient=new CacheUnitClient();
    }

    public void	propertyChange(PropertyChangeEvent evt){

        CacheUnitView view=(CacheUnitView) evt.getSource();
        String request = ((String) evt.getNewValue());
        String response;
        try {
            //send the request and parse the response
            response=cacheUnitClient.send(request);
            String response2;
            if(response.equals("true")) {
                response2="success";
            }
            else if (response.equals("false")){
                response2="fail";
            }
            else if(response.split(" ")[0].equals("\"error:")){
                if(response.contains("\"")) {
                    response2 = ((String) response).split("\"")[1]+";fail";
                }
                else response2=response+";fail";
            }

            else response2=response+";success";

            //Displays the answer on the view
            view.updateUIData(response2);
        } catch (IOException e) {
            view.updateUIData("error: "+e.getMessage());
        }

    }
}
