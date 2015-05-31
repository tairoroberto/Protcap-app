package br.com.trmasolucoes.protcap;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

import br.com.trmasolucoes.protcap.domain.LayoutNoticia;
import br.com.trmasolucoes.protcap.domain.Noticia;
import br.com.trmasolucoes.protcap.util.HttpConnection;


public class MainActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    //comando para ver a fingerkey do android do mau pc
    //keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android

    //Key de acesso a API do Youtube
    private static final String API_KEY = "AIzaSyAagJZVbirJ8Yos-dhuFwDklqrBequ1MHo";

    //Id do video do youtube que será carregado
    //https://www.youtube.com/watch?v=VZoImzA5iO4
    private String id_video = "";

    private YouTubePlayerView youTubePlayer;
    private WebView webIndexTop;
    private WebView webIndexSide;
    private WebView webIndexMain;
    private LinearLayout linearLayoutBanner, linearLayoutNoticia, linearLayoutNoticiaPrincipal,linearLayoutNoticiaSide,linearLayoutVideo;
    private String answer;
    private String answe2;
    private TextView txtNoticeTitle, txtNoticeSubTitle;
    private LayoutNoticia layoutNoticia = new LayoutNoticia();
    private Noticia noticia = new Noticia();
    private ImageView imageViewLogo;
    private Thread mSplashThread;
    private boolean mblnClicou = false;
    private Dialog dialog;
    private VideoView videoView;
    private Uri srcVideo;
    private static final String PREF_NAME = "MainActivity";
    private String serverAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        youTubePlayer = (YouTubePlayerView)findViewById(R.id.youTubePalyer);
        webIndexTop = (WebView)findViewById(R.id.webIndexTop);
        webIndexSide = (WebView)findViewById(R.id.webIndexSide);
        webIndexMain = (WebView)findViewById(R.id.webIndexMain);
        linearLayoutBanner = (LinearLayout)findViewById(R.id.layoutBanner);
        linearLayoutNoticia = (LinearLayout)findViewById(R.id.layoutNoticia);
        linearLayoutNoticiaPrincipal = (LinearLayout)findViewById(R.id.layoutNoticiaPrincipal);
        linearLayoutNoticiaSide = (LinearLayout)findViewById(R.id.layoutNoticiaSide);
        linearLayoutVideo = (LinearLayout)findViewById(R.id.layoutVideo);
        imageViewLogo = (ImageView)findViewById(R.id.imageViewLogo);
        txtNoticeTitle = (TextView)findViewById(R.id.txtTitle);
        txtNoticeSubTitle = (TextView)findViewById(R.id.txtSubTitle);
        videoView = (VideoView)findViewById(R.id.videoView);

        /** In this mode, we can acess this sharedpreference in all Activities and Fragments*/
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        serverAddress = sharedPreferences.getString("serverAddress","http://helpdesk.protcap.com.br:8585/intranet2/public/");


        //configura o webSetings
        WebSettings settingsTop = webIndexTop.getSettings();
        settingsTop.setSupportZoom(false);
        settingsTop.setJavaScriptEnabled(true);

        //configura o webSetings
        WebSettings settingsSide = webIndexSide.getSettings();
        settingsSide.setSupportZoom(false);
        settingsSide.setJavaScriptEnabled(true);
        settingsSide.setSupportZoom(false);

        //configura o webSetings
        WebSettings settingsMain = webIndexMain.getSettings();
        settingsMain.setSupportZoom(false);
        settingsMain.setJavaScriptEnabled(true);
        settingsMain.setSupportZoom(false);


        GetLayoutNoticia getLayoutNoticia = new GetLayoutNoticia(MainActivity.this);
        GetNoticia getNoticia = new GetNoticia(MainActivity.this);

        //Enviaa requisição para o servidor
        ArrayList<NameValuePair> valores = new ArrayList<NameValuePair>();
        valores.add(new BasicNameValuePair("buscar", "true"));



        if (temConexao()){
            getLayoutNoticia.execute(valores);
            getNoticia.execute(valores);


            //para acessa rum pagina
            // String urlTop = "http://helpdesk.protcap.com.br:8585/intranet2/public/index-android-top";
            String urlTop = serverAddress + "index-android-top";
            webIndexTop.loadUrl(urlTop);

            //para acessa rum pagina
           //String urlSide = "http://helpdesk.protcap.com.br:8585/intranet2/public/index-android-side";
            String urlSide = serverAddress + "index-android-side";
            webIndexSide.loadUrl(urlSide);


            /*Verifica se teve erro no webview e mostra o uma mensagem*/

            webIndexTop.setWebViewClient(new WebViewClient() {

                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(MainActivity.this, "SEM CONEXÃO COM INTERNET! - ERRO = "+ description  , Toast.LENGTH_SHORT).show();
                    webIndexTop.setVisibility(View.GONE);
                }
            });

            webIndexSide.setWebViewClient(new WebViewClient() {

                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(MainActivity.this, "SEM CONEXÃO COM INTERNET! - ERRO = "+ description , Toast.LENGTH_SHORT).show();
                    webIndexSide.setVisibility(View.GONE);
                    webIndexMain.setVisibility(View.GONE);
                }
            });


        }else{
            /*Se não houver conexão é mostrada a mensagem*/
            Toast.makeText(MainActivity.this,"SEM CONEXÃO COM INTERNET!",Toast.LENGTH_LONG).show();

            //Intent intent = new Intent("BROADCAST_RECEIVER_START_ACTIVITY");
            /*Intent intent = new Intent(MainActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);*/
            MainActivity.this.finish();
        }








        /**************************************************************************************/
        /**            Thead que roda enquanto espera para abrir tela principal do App			 */
        /*************************** ********************************************************/
        mSplashThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {

                        wait(60000*10);
                        mblnClicou = true;
                    }
                } catch (InterruptedException ex) {
                }
                /**************************************************************************************/
                /**                        Se for clicado Carrega a Activity Principal				 */
                /*************************** ********************************************************/
                if (mblnClicou) {
                    MainActivity.this.finish();

                    //Intent intent = new Intent("BROADCAST_RECEIVER_START_ACTIVITY");
                    Intent intent = new Intent(MainActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
            }
        };

        mSplashThread.start();


        imageViewLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(MainActivity.this);

                /** inflando o layout pra criação do DialogFragment*/
                dialog.setContentView(R.layout.fragment_endereco);


                final EditText edtEderecoLInk = (EditText) dialog.findViewById(R.id.edtEnderecoLink);
                Button btnSalvarFragment = (Button) dialog.findViewById(R.id.btnSalvarFragment);
                Button btnSairFragment = (Button) dialog.findViewById(R.id.btnSairFragment);
                overridePendingTransition(R.anim.push_left_enter, R.anim.push_left_exit);
                dialog.setTitle("Insira o endereço para conexão com servidor");
                dialog.show();

                //seta o valor do shared preference para o editText
                edtEderecoLInk.setText(serverAddress);


                /*************************************************************************/
                /**                    Método para salvar o link do servidor            */
                /***********************************************************************/
                btnSairFragment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                /*************************************************************************/
                /**                    Método para salvar o link do servidor            */
                /***********************************************************************/
                btnSalvarFragment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!edtEderecoLInk.getText().toString().equals("")) {
                            SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("serverAddress", edtEderecoLInk.getText().toString());
                            editor.commit();

                            Toast.makeText(MainActivity.this, "ENDEREÇO DE SERVIDOR ALTERADO", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        } else {
                            Toast.makeText(MainActivity.this, "ENSIRA UM ENDEREÇO VÀLIDO!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean loadAgain) {
        Log.i("Script", "Teste com api do YouTube Raiz 1");
        //verfica se vide já foi carregado
        if (!loadAgain){
            Log.i("Script", "Teste com api do YouTube Raiz 2");
            youTubePlayer.loadVideo(id_video);
        }

        youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
            @Override
            public void onLoading() {

            }

            @Override
            public void onLoaded(String s) {

            }

            @Override
            public void onAdStarted() {

            }

            @Override
            public void onVideoStarted() {

            }

            @Override
            public void onVideoEnded() {
                youTubePlayer.loadVideo(id_video);
            }

            @Override
            public void onError(YouTubePlayer.ErrorReason errorReason) {
                Log.i("Script","Erro - onError: " + errorReason);
            }
        });
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Log.i("Script","Erro - onInitializationFailure: " + youTubeInitializationResult);
    }



    /*******************************************************************************************/
    /**                   Class to make insert event in system                               */
    /*****************************************************************************************/
    private class GetLayoutNoticia extends AsyncTask<ArrayList<NameValuePair>,Void,String> {
        Context context;
        private ProgressDialog progress;

        public GetLayoutNoticia(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(context);
            progress.setMessage("Carregando...");
            progress.show();
        }

        @Override
        protected String doInBackground(ArrayList<NameValuePair>... params) {
            //final String url = "http://helpdesk.protcap.com.br:8585/intranet2/public/android-get-layout-noticia";
            final String url = serverAddress + "android-get-layout-noticia";
            answer = HttpConnection.getSetDataWeb(url, params[0]);
            return answer;
        }

        @Override
        protected void onPostExecute(String answer) {
            super.onPostExecute(answer);
            //Log.i("Script","Resposta do servidor: "+answer);
            if (!answer.equals("Erro")){
                progress.dismiss();
                //Log.i("Script","Resposta: "+answer);

                try{
                    String resposta[] = answer.split("@-@");
                    layoutNoticia.setId(Long.parseLong(resposta[0]));
                    layoutNoticia.setShowMain(resposta[1]);
                    layoutNoticia.setShowSide(resposta[2]);
                    layoutNoticia.setBackgroundColor(resposta[3]);
                    layoutNoticia.setBackgroundColorBanner(resposta[4]);

                    //muda a cor dos layouts coforme a cor que esta guardada no webservice
                    linearLayoutNoticia.setBackgroundColor(Color.parseColor(layoutNoticia.getBackgroundColor()));
                    linearLayoutBanner.setBackgroundColor(Color.parseColor(layoutNoticia.getBackgroundColorBanner()));


                    if (layoutNoticia.getShowMain().equals("none") && layoutNoticia.getShowSide().equals("block")){
                        linearLayoutNoticiaPrincipal.setVisibility(View.GONE);
                        linearLayoutNoticiaSide.setVisibility(View.VISIBLE);
                    }else if (layoutNoticia.getShowMain().equals("block") && layoutNoticia.getShowSide().equals("none")){
                        linearLayoutNoticiaPrincipal.setVisibility(View.VISIBLE);
                        linearLayoutNoticiaSide.setVisibility(View.GONE);
                    }else if (layoutNoticia.getShowMain().equals("none") && layoutNoticia.getShowSide().equals("none")){
                        linearLayoutNoticia.setVisibility(View.GONE);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                        linearLayoutBanner.setLayoutParams(layoutParams);
                    }


                    Log.i("Script","GetLayoutNoticia Resposta servidor: "+answer);

                }catch (Exception e){
                    Log.i("Script","GetLayoutNoticia Erro: "+ e.getMessage());
                    Log.i("Script","GetLayoutNoticia Resposta servidor Erro: "+answer);
                }



            }else{
                progress.dismiss();
                Log.i("Script","GetLayoutNoticia Resposta servidor: "+answer);
            }
        }
    }


    /*******************************************************************************************/
    /**                   Class to make insert event in system                               */
    /*****************************************************************************************/
    private class GetNoticia extends AsyncTask<ArrayList<NameValuePair>,Void,String> {
        Context context;
        private ProgressDialog progress;

        public GetNoticia(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*progress = new ProgressDialog(context);
            progress.setMessage("Carregando...");
            progress.show();*/
        }

        @Override
        protected String doInBackground(ArrayList<NameValuePair>... params) {
            //final String url = "http://helpdesk.protcap.com.br:8585/intranet2/public/android-get-noticia";
            final String url = serverAddress + "android-get-noticia";
            answe2 = HttpConnection.getSetDataWeb(url, params[0]);
            return answe2;
        }

        @Override
        protected void onPostExecute(String answe2) {
            super.onPostExecute(answe2);
            //Log.i("Script","Resposta do servidor: "+answer);
            if (!answe2.equals("Erro")){
                //progress.dismiss();
                //Log.i("Script","Resposta: "+answer);

                try{
                    Log.i("Script", "Resposta noticia:" + answe2);

                    String resposta[] = answe2.split("@-@");
                    noticia.setId(Long.parseLong(resposta[0]));
                    noticia.setTitle(resposta[1]);
                    noticia.setSubTitle(resposta[2]);
                    noticia.setCorTitle(resposta[3]);
                    noticia.setCorsubTitle(resposta[4]);
                    noticia.setMidia(resposta[5]);
                    noticia.setTipoMidia(resposta[6]);
                    noticia.setTipoVideo(resposta[7]);



                    Log.i("Script", "Id:" + noticia.getId());
                    Log.i("Script","TITLE:" + noticia.getTitle());
                    Log.i("Script","SUBTITLE:" + noticia.getSubTitle());
                    Log.i("Script",",COR-TITLE:" + noticia.getCorTitle());
                    Log.i("Script","COR-SUBTITLE:" + noticia.getCorsubTitle());
                    Log.i("Script","MIDIA:" + noticia.getMidia());
                    Log.i("Script","TIPO-MIDIA:" + noticia.getTipoMidia());
                    Log.i("Script","TIPO-VIDEO:" + noticia.getTipoVideo());
                    Log.i("Script","Servidor e midia:" + serverAddress + noticia.getMidia());

                    //verifica se midia esta nula, e se a midia for um link do you tube esconde o webview
                    //e mostra so o youtubeView
                    if (noticia.getTipoMidia().equals("video")){
                        //se tipo do video for == YouTube carrega a Api do yutube senão carega o video por url
                        if (noticia.getTipoVideo().equals("YouTube")){
                            String[] video = noticia.getMidia().split("=");

                            id_video = video[1];


                            webIndexMain.setVisibility(View.GONE);
                            videoView.setVisibility(View.GONE);
                            linearLayoutVideo.setVisibility(View.VISIBLE);
                            youTubePlayer.setVisibility(View.VISIBLE);
                            youTubePlayer.initialize(API_KEY, MainActivity.this);

                            /*txtNoticeTitle.setText(noticia.getTitle());
                            txtNoticeSubTitle.setText(noticia.getSubTitle());*/
                            txtNoticeTitle.setText(noticia.getTitle());
                            txtNoticeSubTitle.setText(noticia.getSubTitle());
                            txtNoticeTitle.setTextColor(Color.parseColor(noticia.getCorTitle()));
                            txtNoticeSubTitle.setTextColor(Color.parseColor(noticia.getCorsubTitle()));

                        }else{

                            webIndexMain.setVisibility(View.GONE);
                            linearLayoutVideo.setVisibility(View.VISIBLE);
                            videoView.setVisibility(View.VISIBLE);
                            youTubePlayer.setVisibility(View.GONE);

                            txtNoticeTitle.setText(noticia.getTitle());
                            txtNoticeSubTitle.setText(noticia.getSubTitle());
                            txtNoticeTitle.setTextColor(Color.parseColor(noticia.getCorTitle()));
                            txtNoticeSubTitle.setTextColor(Color.parseColor(noticia.getCorsubTitle()));

                            srcVideo = Uri.parse(serverAddress + noticia.getMidia());
                            //pega um video da internet

                            videoView.setVideoURI(srcVideo);
                            //add os controles do video

                            videoView.start();
                            videoView.setMediaController(new MediaController(MainActivity.this));

                            //Repetir video
                            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    videoView.seekTo(0);
                                    videoView.start();
                                }
                            });

                            //video em fullscreen em outra tela
                            txtNoticeTitle.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    videoView.pause();

                                    Intent intent = new Intent(MainActivity.this,VideoFullActivity.class);
                                    intent.putExtra("srcVideo", srcVideo.toString());
                                    intent.putExtra("time",videoView.getCurrentPosition());
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });
                        }


                    }else{
                        webIndexMain.setVisibility(View.VISIBLE);
                        youTubePlayer.setVisibility(View.GONE);
                        linearLayoutVideo.setVisibility(View.GONE);

                        //para acessa rum pagina
                       //String urlMain = "http://helpdesk.protcap.com.br:8585/intranet2/public/index-android-main";
                        String urlMain = serverAddress + "index-android-main";
                        webIndexMain.loadUrl(urlMain);
                    }

                    Log.i("Script","GetNoticia Resposta servidor: "+answe2);
                }catch (Exception e){
                    Log.i("Script","GetNoticia Erro: "+ e.getMessage());
                    Log.i("Script","GetNoticia Resposta servidor Erro: "+answer);
                }



            }else{
               // progress.dismiss();
                Log.i("Script","GetNoticia Resposta servidor: "+answe2);
            }
        }
    }

    /**********************************************************************************************/
    /**	 					Método para verificar se há conexão com internet		 		   	 */
    /********************************************************************************************/
    private boolean temConexao() {
        boolean lblnRet = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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



    public static String UTF8toISO(String str){
        try {
            return new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

}
