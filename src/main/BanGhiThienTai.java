package main;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class BanGhiThienTai {
    private String maBanGhi;
    private LocalDateTime thoiGianGhiNhan;
    private ThietHaiVeNguoi thietHaiNguoi;                   
    private ArrayList<ThietHaiVatChat> dsThietHaiVatChat = new ArrayList<>(); 
    private KhuVuc khuVuc;
    private ThienTai thienTai;                             

    public BanGhiThienTai(String maBanGhi, LocalDateTime thoiGianGhiNhan, KhuVuc khuVuc) {
        this.maBanGhi = maBanGhi;
        this.thoiGianGhiNhan = thoiGianGhiNhan;
        this.khuVuc = khuVuc;
        this.thietHaiNguoi = new ThietHaiVeNguoi();
    }

    public void capNhatThoiGianGhiNhan(LocalDateTime thoiGian) { 
//    	this.thoiGianGhiNhan = thoiGian; 
    	}

    public void capNhatThietHaiNguoi(ThietHaiVeNguoi thietHai) {
//        if (thietHai != null) 
//        	this.thietHaiNguoi = thietHai;
    }

    public boolean capNhatThietHaiVatChat(ThietHaiVatChat thietHai) {
//        if (thietHai == null || !thietHai.kiemTraHopLe()) 
        	return false;
//        return dsThietHaiVatChat.add(thietHai);
    }

    public double tinhTongThietHaiVatChat() {
//        double tong = 0;
//        for (ThietHaiVatChat t : dsThietHaiVatChat) tong += t.tinhTongThietHai();
//        return tong;
    	return 0;
    }

    public boolean kiemTraHopLe() {
//        if (maBanGhi == null || thoiGianGhiNhan == null || khuVuc == null || !thietHaiNguoi.kiemTraHopLe())
//            return false;
//        for (ThietHaiVatChat t : dsThietHaiVatChat) if (!t.kiemTraHopLe()) return false;
        return true;
    }
//getter-setter
    void setThienTai(ThienTai thienTai) { 
    	this.thienTai = thienTai; 
    	}
    public String getMaBanGhi() { 
    	return maBanGhi; 
    	}
    public LocalDateTime getThoiGianGhiNhan() { 
    	return thoiGianGhiNhan; 
    	}
    public ThietHaiVeNguoi getThietHaiNguoi() { 
    	return thietHaiNguoi; 
    	}
    public ArrayList<ThietHaiVatChat> getDsThietHaiVatChat() { 
    	return dsThietHaiVatChat; 
    	}
    public KhuVuc getKhuVuc() { 
    	return khuVuc; 
    	}
    public ThienTai getThienTai() { 
    	return thienTai; 
    	}
}
