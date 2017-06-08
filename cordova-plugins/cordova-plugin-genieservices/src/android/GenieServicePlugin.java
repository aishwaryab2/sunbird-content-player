package org.ekstep.geniecanvas;

import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.ekstep.genieresolvers.GenieSDK;
import org.ekstep.genieresolvers.content.ContentService;
import org.ekstep.genieresolvers.user.UserService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GenieServicePlugin extends CordovaPlugin {

	public static final String TAG = "Genie Service Plugin";

//	private TelemetryService telemetry;
    private UserService userService;
//    private GenieServices genieServices;
   private ContentService contentService;
//    private Summarizer summarizer;
//    private Language language;
    private GenieSDK genieSdk;

	public GenieServicePlugin() {
		System.out.println("Genie Service Constructor..........");
    }

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);

    }


    public boolean execute(final String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        CordovaActivity activity = (CordovaActivity) this.cordova.getActivity();

        genieSdk = GenieSDK.init(activity, "org.ekstep.genieservices");
//
//        if (null == telemetry) {
//            if (null != activity) {
//                telemetry = genieSdk.getUserService(); //new Telemetry(activity);
//            }
//        }
        if(null == userService) {
            if (null != activity) {
                userService = genieSdk.getUserService(); //new UserProfile(activity);
            }
        }
//        if(null == genieServices) {
//            if (null != activity) {
//                genieServices = new GenieServices(activity);
//            }
//        }
       if(null == contentService) {
           if(null != activity) {
               contentService = genieSdk.getContentService();
           }
       }
//        if(null == summarizer) {
//            if(null != activity) {
//                summarizer = new Summarizer(activity);
//            }
//        }
//        if(null == language) {
//            if(null != activity) {
//                language = new Language(activity);
//            }
//        }
        // if(null == summarizer) {
        //     if(null != activity) {
        //         feedbackService = new FeedbackService(activity);
        //     }
        // }
        Log.v(TAG, "GenieServicePlugin received:" + action);
        System.out.println("Genie Service action: " + action);
        if(action.equals("sendTelemetry")) {
            if (args.length() != 1) {
                callbackContext.error(getErrorJSONObject("INVALID_ACTION", null));
            	return false;
			}
            String data = args.getString(0);
            sendTelemetry(data, callbackContext);
        } else if(action.equals("getCurrentUser")) {
            userService.getCurrentUser(new UserProfileResponse(callbackContext));
        } else if(action.equals("getContent")) {
            String contentId = args.getString(0);
            contentService.getContent(contentId, new GenieServicesResponse(callbackContext));
        }/* else if(action.equals("getRelatedContent")) {
            String uid = args.getString(0);
            List<HashMap<String, Object>> filterList = new ArrayList<HashMap<String, Object>>();
            JSONArray jsonArray = args.getJSONArray(1);
            if(jsonArray != null && jsonArray.length() > 0) {
                for(int i=0;i<jsonArray.length();i++) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    Iterator keys = jsonArray.getJSONObject(i).keys();
                    while (keys.hasNext()) {
                        String key = (String) keys.next();
                        map.put(key, jsonArray.getJSONObject(i).get(key));
                    }
                    filterList.add(map);
                }

            }
            content.getRelatedContent(uid, filterList, new GenieServicesResponse(callbackContext));
        }
        else if(action.equals("sendFeedback")) {
            String evt = args.getString(0);
            content.sendFeedback(evt, new TelemetryResponse(callbackContext));
        }else if(action.equals("getLearnerAssessment")) {
            String uid = args.getString(0);
            String contentId = args.getString(1);
            summarizer.getLearnerAssessment(uid, contentId, new GenieServicesResponse(callbackContext));
        } else if(action.equals("getContentList")) {
            String[] filter = null;
            JSONArray jsonArray = args.getJSONArray(0);
            if(jsonArray != null && jsonArray.length() > 0) {
                filter = new String[jsonArray.length()];
                List<String> filterList = new ArrayList<String>();
                for(int i=0;i<jsonArray.length();i++)
                    filterList.add(jsonArray.getString(i));
                filterList.toArray(filter);
            } else {
                filter = new String[0];
            }
            if(filter.length == 0) {
                content.getList(new GenieServicesListResponse(callbackContext));
            } else {
                content.filter(filter, new GenieServicesListResponse(callbackContext));
            }
        } else if(action.equals("languageSearch")) {
            String inputFilter = args.getString(0);
            language.languageSearch(inputFilter, new GenieServicesResponse(callbackContext));
        }else if("endGenieCanvas".equals(action)) {
            System.out.println("*** Activity:" + activity);
            activity.finish();
        }*/
        return true;
    }

    private void sendTelemetry(String data, CallbackContext callbackContext) {
//    	if (null != data) {
//    		telemetry.send(data, new TelemetryResponse(callbackContext));
//    	}
    }

    private JSONObject getErrorJSONObject(String errorCode, String errorParam) {
    	Map<String, Object> error = new HashMap<String, Object>();
        error.put("status", "error");
        JSONObject obj = new JSONObject(error);
        try {
        	if (null != errorCode)
            	error.put("errorCode", errorCode);
            if (null != errorParam)
                error.put("errorParam", errorParam);
            obj = new JSONObject(error);
        } catch(Exception e) {
        }
        return obj;
    }

}
