package main;

import java.util.HashMap;

public class KhuVuc {
    private String maKV;
    private String tinhThanhPho;
    private String quanHuyen;
    private String xaPhuong;
    private HashMap<String, HoDan> dsHoDan = new HashMap<>();

    public KhuVuc(String maKV, String tinhThanhPho, String quanHuyen, String xaPhuong) {
        this.maKV = maKV;
        capNhatThongTin(tinhThanhPho, quanHuyen, xaPhuong);
    }

    public void capNhatThongTin(String tinhThanhPho, String quanHuyen, String xaPhuong) {
        this.tinhThanhPho = tinhThanhPho;
        this.quanHuyen = quanHuyen;
        this.xaPhuong = xaPhuong;
    }

    public boolean themHoDan(HoDan hoDan) {
//        if (hoDan == null || dsHoDan.containsKey(hoDan.getMaHo())) return false;
//        dsHoDan.put(hoDan.getMaHo(), hoDan);
        return true;
    }

    public boolean xoaHoDan(String maHo) { 
//    	return dsHoDan.remove(maHo) != null; 
    	return false;
    	}
    public HoDan timHoDan(String maHo) { 
//    	return dsHoDan.get(maHo); 
    	return null;
    	}
    public int demSoHoDan() { 
    	return dsHoDan.size(); 
    	}

	public String getMaKV() {
		return maKV;
	}

	public String getTinhThanhPho() {
		return tinhThanhPho;
	}

	public String getQuanHuyen() {
		return quanHuyen;
	}

	public String getXaPhuong() {
		return xaPhuong;
	}
  
}
