package iths.com.food.helper.sms;

import android.content.Context;
// Dialog
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;
// SMS
import android.telephony.SmsManager;
// View
import android.text.InputType;
import android.widget.EditText;
import iths.com.food.R;
// Model
import iths.com.food.model.IMeal;

/**
 * Creates a popup window so user can enter the phone number of the recipient.
 * Then it sends a sms containing: the apps name, the meals name, the meals id.
 * A toast is shown to the user that the message is send.
 */
public class SmsSender {

    /**
     * Creates a popup window for entering the phone number and sends a sms containing the apps name,
     * the meals name and the meals id.
     *
     * sms syntax: appName mealName mealId
     */
    public static void sendSms(final Context context, final IMeal meal) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enter recipients phone number, inc. country code");

        // Set up the input
        final EditText input = new EditText(context);

        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_PHONE);

        builder.setView(input);

        // Set up the buttons
        // Button Ok
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                String phoneNumber = input.getText().toString();
                String appName = context.getResources().getString(R.string.app_name);
                String msg = "*" + appName + "* Meal: " + meal.getName() + " MealId: " + meal.getId() +
                        ". To reply use the following syntax: *FoodFlash* MealId: [nr] Tasty: [nr] Healthy: [nr]";

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, msg, null, null);

                Toast.makeText(context, "sms is sent", Toast.LENGTH_SHORT).show();
            }
        });

        // Button Cancel
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
