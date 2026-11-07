package main;

import java.util.ArrayList;

public class HoDan {
    private String maHo;
    private String diaChi;
    private NguoiDan chuHo;
    private ArrayList<NguoiDan> dsThanhVien = new ArrayList<>();

    public HoDan(String maHo, String diaChi, NguoiDan chuHo) {
        this.maHo = maHo;
        this.diaChi = diaChi;
        this.chuHo = chuHo;
        if (chuHo != null) dsThanhVien.add(chuHo);
    }

    public void capNhatDiaChi(String diaChi) { 
    	this.diaChi = diaChi; 
    	}

    public boolean themThanhVien(NguoiDan nd) {
//        if (nd == null || timThanhVien(nd.getCCCD()) != null) return false;
//        return dsThanhVien.add(nd);
    	return false;
    }
    public boolean xoaThanhVien(String cccd) {
//        NguoiDan nd = timThanhVien(cccd);
//        if (nd == null || nd == chuHo) return false; 
//        return dsThanhVien.remove(nd);
    	return false;
    }

    public NguoiDan timThanhVien(String cccd) {
//        for (NguoiDan nd : dsThanhVien)
//            if (nd.getCCCD().equals(cccd)) return nd;
        return null;
    }

    public int demSoThanhVien() { 
    	return dsThanhVien.size(); 
    	}
    public String getMaHo() { 
    	return maHo; 
    	}
    public String getDiaChi() { 
    	return diaChi; 
    	}
    public NguoiDan getChuHo() { 
    	return chuHo; 
    	}
    public ArrayList<NguoiDan> getDsThanhVien() { 
    	return dsThanhVien; 
    	}
}
