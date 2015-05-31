package br.com.trmasolucoes.protcap.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tairo on 02/04/15.
 */
public class Noticia implements Parcelable{
    private long id;
    private String title;
    private String subTitle;
    private String corTitle;
    private String corsubTitle;
    private String midia;
    private String tipoMidia;
    private String tipoVideo;


    public Noticia() {
    }

    public Noticia(long id, String title, String subTitle, String midia, String tipoMidia, String tipoVideo) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.midia = midia;
        this.tipoMidia = tipoMidia;
        this.tipoVideo = tipoVideo;
    }

    public Noticia(Parcel parcel) {
        this.id = parcel.readLong();
        this.title = parcel.readString();
        this.subTitle = parcel.readString();
        this.corTitle = parcel.readString();
        this.corsubTitle = parcel.readString();
        this.midia = parcel.readString();
        this.tipoMidia = parcel.readString();
        this.tipoVideo = parcel.readString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getCorTitle() {
        return corTitle;
    }

    public void setCorTitle(String corTitle) {
        this.corTitle = corTitle;
    }

    public String getCorsubTitle() {
        return corsubTitle;
    }

    public void setCorsubTitle(String corsubTitle) {
        this.corsubTitle = corsubTitle;
    }

    public String getMidia() {
        return midia;
    }

    public void setMidia(String midia) {
        this.midia = midia;
    }

    public String getTipoMidia() {
        return tipoMidia;
    }

    public void setTipoMidia(String tipoMidia) {
        this.tipoMidia = tipoMidia;
    }

    public String getTipoVideo() {
        return tipoVideo;
    }

    public void setTipoVideo(String tipoVideo) {
        this.tipoVideo = tipoVideo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //Escreve os valores que serão transportados
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(subTitle);
        dest.writeString(corTitle);
        dest.writeString(corsubTitle);
        dest.writeString(midia);
        dest.writeString(tipoMidia);
        dest.writeString(tipoVideo);
    }

    //Obs: Obrigatorio usar o CREATOR para recuperar os dados do parcelable
    public static final Parcelable.Creator<Noticia> CREATOR = new Parcelable.Creator<Noticia>() {

        //Pega o nosso parcel e instacia para podermos utilizar o nosso objeto "No caso o Studant"
        @Override
        public Noticia createFromParcel(Parcel source) {
            return new Noticia(source);
        }

        @Override
        public Noticia[] newArray(int size) {
            return new Noticia[size];
        }
    };
}
