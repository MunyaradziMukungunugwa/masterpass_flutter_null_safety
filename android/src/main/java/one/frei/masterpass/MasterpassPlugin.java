// package one.frei.masterpass;

// import java.util.HashMap;

// import android.app.Activity;
// import android.content.Context;
// import android.content.Intent;

// import androidx.annotation.NonNull;

// import io.flutter.embedding.engine.plugins.FlutterPlugin;
// import io.flutter.embedding.engine.plugins.activity.ActivityAware;
// import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
// import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding.OnActivityResultListener;
// import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding.StartActivityForResultListener;
// import io.flutter.plugin.common.MethodCall;
// import io.flutter.plugin.common.MethodChannel;
// import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
// import io.flutter.plugin.common.MethodChannel.Result;

// import com.oltio.liblite.activity.LibLiteActivity;

// /**
//  * MasterpassPlugin
//  */
// public class MasterpassPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware, StartActivityForResultListener {
//     private static final int REQUEST_CODE = 10;
//     private MethodChannel channel;
//     private Activity activity;
//     private Result result;

//     @Override
//     public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
//         channel = new MethodChannel(binding.getBinaryMessenger(), "masterpass");
//         channel.setMethodCallHandler(this);
//     }

//     @Override
//     public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
//         channel.setMethodCallHandler(null);
//     }

//     @Override
//     public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
//         activity = binding.getActivity();
//         binding.addActivityResultListener(this);
//     }

//     @Override
//     public void onDetachedFromActivityForConfigChanges() {
//         activity = null;
//     }

//     @Override
//     public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
//         activity = binding.getActivity();
//         binding.addActivityResultListener(this);
//     }

//     @Override
//     public void onDetachedFromActivity() {
//         activity = null;
//     }

//     @Override
//     public void onActivityResult(int requestCode, int resultCode, Intent data) {
//         if (requestCode == REQUEST_CODE) {
//             if (resultCode == LibLiteActivity.LIBLITE_ERROR) {
//                 CheckoutResult checkoutResult = new CheckoutResult("OUT_ERROR_CODE", "no ref. for this result");
//                 result.success(checkoutResult.toHashMap());
//             } else if (resultCode == LibLiteActivity.LIBLITE_PAYMENT_SUCCESS) {
//                 CheckoutResult checkoutResult = new CheckoutResult("PAYMENT_SUCCEEDED", data.getStringExtra(LibLiteActivity.OUT_TX_REFERENCE));
//                 result.success(checkoutResult.toHashMap());
//             } else if (resultCode == LibLiteActivity.LIBLITE_PAYMENT_FAILED) {
//                 CheckoutResult checkoutResult = new CheckoutResult("PAYMENT_FAILED", data.getStringExtra(LibLiteActivity.OUT_TX_REFERENCE));
//                 result.success(checkoutResult.toHashMap());
//             } else if (resultCode == LibLiteActivity.LIBLITE_USER_CANCELLED) {
//                 CheckoutResult checkoutResult = new CheckoutResult("USER_CANCELLED", "no ref. for this result");
//                 result.success(checkoutResult.toHashMap());
//             } else if (resultCode == LibLiteActivity.LIBLITE_INVALID_CODE) {
//                 CheckoutResult checkoutResult = new CheckoutResult("INVALID_CODE", "no ref. for this result");
//                 result.success(checkoutResult.toHashMap());
//             }
//         }
//     }

//     @Override
//     public void onMethodCall(MethodCall call, Result result) {
//         this.result = result;
//         if (call.method.equals("checkout")) {
//             doMasterpassCheckout(
//                     call.argument("code"),
//                     call.argument("system"),
//                     call.argument("key")
//             );
//         } else {
//             result.notImplemented();
//         }
//     }

//     private void doMasterpassCheckout(String code, String system, String key) {
//         Intent intent = new Intent(activity, LibLiteActivity.class);
//         intent.putExtra(LibLiteActivity.IN_CODE, code);
//         intent.putExtra(LibLiteActivity.IN_API_KEY, key);
//         intent.putExtra(LibLiteActivity.IN_SYSTEM, system);
//         activity.startActivityForResult(intent, REQUEST_CODE);
//     }

//     /**
//      * Models a masterpass checkout result. An instance of this class is returned to the Flutter plugin
//      * as a HashMap.
//      */
//     private static class CheckoutResult {
//         private final String code;
//         private final String reference;

//         public CheckoutResult(String code, String reference) {
//             this.code = code;
//             this.reference = reference;
//         }

//         public HashMap<String, String> toHashMap() {
//             HashMap<String, String> map = new HashMap<>();
//             map.put("code", code);
//             map.put("reference", reference);

//             return map;
//         }
//     }
// }

package one.frei.masterpass;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.ActivityResultListener;

import com.oltio.liblite.activity.LibLiteActivity;

/**
 * MasterpassPlugin
 */
public class MasterpassPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware, ActivityResultListener {
    private static final int REQUEST_CODE = 10;
    private MethodChannel channel;
    private Activity activity;
    private Result result;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        channel = new MethodChannel(binding.getBinaryMessenger(), "masterpass");
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        activity = binding.getActivity();
        binding.addActivityResultListener(this);
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        activity = null;
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        activity = binding.getActivity();
        binding.addActivityResultListener(this);
    }

    @Override
    public void onDetachedFromActivity() {
        activity = null;
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == LibLiteActivity.LIBLITE_ERROR) {
                CheckoutResult checkoutResult = new CheckoutResult("OUT_ERROR_CODE", "no ref. for this result");
                result.success(checkoutResult.toHashMap());
            } else if (resultCode == LibLiteActivity.LIBLITE_PAYMENT_SUCCESS) {
                CheckoutResult checkoutResult = new CheckoutResult("PAYMENT_SUCCEEDED", data.getStringExtra(LibLiteActivity.OUT_TX_REFERENCE));
                result.success(checkoutResult.toHashMap());
            } else if (resultCode == LibLiteActivity.LIBLITE_PAYMENT_FAILED) {
                CheckoutResult checkoutResult = new CheckoutResult("PAYMENT_FAILED", data.getStringExtra(LibLiteActivity.OUT_TX_REFERENCE));
                result.success(checkoutResult.toHashMap());
            } else if (resultCode == LibLiteActivity.LIBLITE_USER_CANCELLED) {
                CheckoutResult checkoutResult = new CheckoutResult("USER_CANCELLED", "no ref. for this result");
                result.success(checkoutResult.toHashMap());
            } else if (resultCode == LibLiteActivity.LIBLITE_INVALID_CODE) {
                CheckoutResult checkoutResult = new CheckoutResult("INVALID_CODE", "no ref. for this result");
                result.success(checkoutResult.toHashMap());
            }
        }
        return true;
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        this.result = result;
        if (call.method.equals("checkout")) {
            doMasterpassCheckout(
                    call.argument("code"),
                    call.argument("system"),
                    call.argument("key")
            );
        } else {
            result.notImplemented();
        }
    }

    private void doMasterpassCheckout(String code, String system, String key) {
        Intent intent = new Intent(activity, LibLiteActivity.class);
        intent.putExtra(LibLiteActivity.IN_CODE, code);
        intent.putExtra(LibLiteActivity.IN_API_KEY, key);
        intent.putExtra(LibLiteActivity.IN_SYSTEM, system);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    /**
     * Models a masterpass checkout result. An instance of this class is returned to the Flutter plugin
     * as a HashMap.
     */
    private static class CheckoutResult {
        private final String code;
        private final String reference;

        public CheckoutResult(String code, String reference) {
            this.code = code;
            this.reference = reference;
        }

        public HashMap<String, String> toHashMap() {
            HashMap<String, String> map = new HashMap<>();
            map.put("code", code);
            map.put("reference", reference);

            return map;
        }
    }
}

