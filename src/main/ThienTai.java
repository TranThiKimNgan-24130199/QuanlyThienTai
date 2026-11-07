package main;

import java.time.LocalDateTime;
import java.util.TreeMap;

import trangthai.LoaiThienTai;
import trangthai.TrangThaiThienTai;

public class ThienTai {
    private String maTT;
    private String ten;
    private LoaiThienTai loaiThienTai;
    private LocalDateTime thoiGianBatDau;
    private LocalDateTime thoiGianKetThuc;   
    private String moTa;
    private TrangThaiThienTai trangThai = TrangThaiThienTai.DANG_DIEN_RA;
    private TreeMap<String, BanGhiThienTai> dsBanGhi = new TreeMap<>(); // key = maBanGhi
    private KhacPhucThienTai khacPhuc;

    public ThienTai(String maTT, String ten, LoaiThienTai loaiThienTai,
                    LocalDateTime thoiGianBatDau, String moTa) {
        this.maTT = maTT;
        capNhatThongTin(ten, loaiThienTai, thoiGianBatDau, moTa);
    }

    public void capNhatThongTin(String ten, LoaiThienTai loaiThienTai,
                                LocalDateTime thoiGianBatDau, String moTa) {
        this.ten = ten;
        this.loaiThienTai = loaiThienTai;
        this.thoiGianBatDau = thoiGianBatDau;
        this.moTa = moTa;
    }

    public boolean ketThucSuKien(LocalDateTime thoiGian) {
//        if (trangThai == TrangThaiThienTai.DA_KET_THUC || thoiGian == null
//                || thoiGian.isBefore(thoiGianBatDau)) return false;
//        this.thoiGianKetThuc = thoiGian;
//        this.trangThai = TrangThaiThienTai.DA_KET_THUC;
        return true;
    }

    public boolean themBanGhiThietHai(BanGhiThienTai banGhi) {
//        if (banGhi == null || trangThai == TrangThaiThienTai.DA_KET_THUC
//                || dsBanGhi.containsKey(banGhi.getMaBanGhi()) || !banGhi.kiemTraHopLe()) return false;
//        dsBanGhi.put(banGhi.getMaBanGhi(), banGhi);
//        banGhi.setThienTai(this);
        return true;
    }

    public void setKhacPhuc(KhacPhucThienTai khacPhuc) { 
    	this.khacPhuc = khacPhuc; 
    	}

    public double tinhTongThietHaiVatChat() {
//        double tong = 0;
//        for (BanGhiThienTai b : dsBanGhi.values()) tong += b.tinhTongThietHaiVatChat();
//        return tong;
    	return 0;
    }

    public int tinhTongNguoiChet() {
//        int tong = 0;
//        for (BanGhiThienTai b : dsBanGhi.values()) tong += b.getThietHaiNguoi().getSoNguoiChet();
//        return tong;
    	return 0;
    }

    public int tinhTongNguoiBiThuong() {
//        int tong = 0;
//        for (BanGhiThienTai b : dsBanGhi.values()) tong += b.getThietHaiNguoi().getSoNguoiBiThuong();
//        return tong;
    	return 0;
    }

    public int tinhTongNguoiMatTich() {
//        int tong = 0;
//        for (BanGhiThienTai b : dsBanGhi.values()) tong += b.getThietHaiNguoi().getSoNguoiMatTich();
//        return tong;
    	return 0;
    }

	public String getMaTT() {
		return maTT;
	}

	public String getTen() {
		return ten;
	}

	public LoaiThienTai getLoaiThienTai() {
		return loaiThienTai;
	}

	public LocalDateTime getThoiGianBatDau() {
		return thoiGianBatDau;
	}

	public LocalDateTime getThoiGianKetThuc() {
		return thoiGianKetThuc;
	}

	public String getMoTa() {
		return moTa;
	}

	public TrangThaiThienTai getTrangThai() {
		return trangThai;
	}

	public TreeMap<String, BanGhiThienTai> getDsBanGhi() {
		return dsBanGhi;
	}

	public KhacPhucThienTai getKhacPhuc() {
		return khacPhuc;
	}

  
}
