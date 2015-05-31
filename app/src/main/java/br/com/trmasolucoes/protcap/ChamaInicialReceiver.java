package br.com.trmasolucoes.protcap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class ChamaInicialReceiver extends BroadcastReceiver {
    private Context context;
    public ChamaInicialReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        this.context = context;
        Intent it = new Intent(context, MainActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
    }



    /**********************************************************************************************/
    /**	 					Método para verificar se há conexão com internet		 		   	 */
    /********************************************************************************************/
    private boolean temConexao() {
        boolean lblnRet = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm.getActiveNetworkInfo() != null
                    && cm.getActiveNetworkInfo().isAvailable()
                    && cm.getActiveNetworkInfo().isConnected()) {
                lblnRet = true;
            } else {
                lblnRet = false;
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return lblnRet;
    }

}
