package com.kuangxf.baseappas.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by kuangxf on 2017/7/31.
 */
@Table(name = "DeviceIdBean")
public class DeviceIdBean {
    @Column(name = "id", isId = true)
    private int id;
    @Column(name = "imeiId")
    private String imeiId;
    @Column(name = "androidId")
    private String androidId;
    @Column(name = "macAddressId")
    private String macAddressId;
    @Column(name = "blueToothAddressId")
    private String blueToothAddressId;
    @Column(name = "idFromRomId")
    private String idFromRomId;
    @Column(name = "uuidId")
    private String uuidId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImeiId() {
        return imeiId;
    }

    public void setImeiId(String imeiId) {
        this.imeiId = imeiId;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getMacAddressId() {
        return macAddressId;
    }

    public void setMacAddressId(String macAddressId) {
        this.macAddressId = macAddressId;
    }

    public String getBlueToothAddressId() {
        return blueToothAddressId;
    }

    public void setBlueToothAddressId(String blueToothAddressId) {
        this.blueToothAddressId = blueToothAddressId;
    }

    public String getIdFromRomId() {
        return idFromRomId;
    }

    public void setIdFromRomId(String idFromRomId) {
        this.idFromRomId = idFromRomId;
    }

    public String getUuidId() {
        return uuidId;
    }

    public void setUuidId(String uuidId) {
        this.uuidId = uuidId;
    }

    @Override
    public String toString() {
        return "DeviceIdBean{" +
                "id=" + id +
                ", imeiId='" + imeiId + '\'' +
                ", androidId='" + androidId + '\'' +
                ", macAddressId='" + macAddressId + '\'' +
                ", blueToothAddressId='" + blueToothAddressId + '\'' +
                ", idFromRomId='" + idFromRomId + '\'' +
                ", uuidId='" + uuidId + '\'' +
                '}';
    }
}
