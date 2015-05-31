package br.com.trmasolucoes.protcap.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tairo on 02/04/15.
 */
public class LayoutNoticia implements Parcelable{

    private long id;
    private String showMain;
    private String showSide;
    private String backgroundColor;
    private String backgroundColorBanner;

    public LayoutNoticia() {
    }

    public LayoutNoticia(long id, String showMain, String showSide, String backgroundColor, String backgroundColorBanner) {
        this.id = id;
        this.showMain = showMain;
        this.showSide = showSide;
        this.backgroundColor = backgroundColor;
        this.backgroundColorBanner = backgroundColorBanner;
    }

    public LayoutNoticia(Parcel parcel) {
        this.id = parcel.readLong();
        this.showMain = parcel.readString();
        this.showSide = parcel.readString();
        this.backgroundColor = parcel.readString();
        this.backgroundColorBanner = parcel.readString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getShowMain() {
        return showMain;
    }

    public void setShowMain(String showMain) {
        this.showMain = showMain;
    }

    public String getShowSide() {
        return showSide;
    }

    public void setShowSide(String showSide) {
        this.showSide = showSide;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getBackgroundColorBanner() {
        return backgroundColorBanner;
    }

    public void setBackgroundColorBanner(String backgroundColorBanner) {
        this.backgroundColorBanner = backgroundColorBanner;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //Escreve os valores que serão transportados
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(showMain);
        dest.writeString(showSide);
        dest.writeString(backgroundColor);
        dest.writeString(showMain);
        dest.writeString(backgroundColorBanner);
    }

    //Obs: Obrigatorio usar o CREATOR para recuperar os dados do parcelable
    public static final Creator<LayoutNoticia> CREATOR = new Creator<LayoutNoticia>() {

        //Pega o nosso parcel e instacia para podermos utilizar o nosso objeto "No caso o Studant"
        @Override
        public LayoutNoticia createFromParcel(Parcel source) {
            return new LayoutNoticia(source);
        }

        @Override
        public LayoutNoticia[] newArray(int size) {
            return new LayoutNoticia[size];
        }
    };
}
