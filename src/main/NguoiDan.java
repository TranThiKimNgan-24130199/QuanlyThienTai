package main;

import java.time.LocalDate;

import trangthai.GioiTinh;
import trangthai.QuanHeChuHo;
import trangthai.TinhTrangHonNhan;

public class NguoiDan {
    private String CCCD;
    private String hoTen;
    private GioiTinh gioiTinh;
    private LocalDate ngaySinh;
    private TinhTrangHonNhan tinhTrangHonNhan;
    private boolean conSong = true;
    private QuanHeChuHo quanHeChuHo;

    public NguoiDan(String CCCD, String hoTen, GioiTinh gioiTinh, LocalDate ngaySinh,
                    TinhTrangHonNhan tinhTrangHonNhan, QuanHeChuHo quanHeChuHo) {
        this.CCCD = CCCD;
        capNhatThongTin(hoTen, gioiTinh, ngaySinh, tinhTrangHonNhan, quanHeChuHo);
    }

    public void capNhatThongTin(String hoTen, GioiTinh gioiTinh, LocalDate ngaySinh,
                                TinhTrangHonNhan tinhTrangHonNhan, QuanHeChuHo quanHeChuHo) {
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.tinhTrangHonNhan = tinhTrangHonNhan;
        this.quanHeChuHo = quanHeChuHo;
    }

	public String getCCCD() {
		return CCCD;
	}

	public String getHoTen() {
		return hoTen;
	}

	public GioiTinh getGioiTinh() {
		return gioiTinh;
	}

	public LocalDate getNgaySinh() {
		return ngaySinh;
	}

	public TinhTrangHonNhan getTinhTrangHonNhan() {
		return tinhTrangHonNhan;
	}

	public boolean isConSong() {
		return conSong;
	}

	public QuanHeChuHo getQuanHeChuHo() {
		return quanHeChuHo;
	}

//    public void setConSong(boolean conSong) { 
//    	this.conSong = conSong; 
//    	}
    
}

