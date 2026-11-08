
package main;
//Người thực hiện: Nguyễn Thị Hoài Lan
import java.time.LocalDate;

import trangthai.*;

public class NguoiDan {

	// Ma dinh danh duy nhat cua nguoi dan, khong thay doi sau khi khoi tao
	private final String CCCD;

	private String hoTen;

	private GioiTinh gioiTinh;

	private LocalDate ngaySinh;

	private TinhTrangHonNhan tinhTrangHonNhan;

	// Tinh trang song hien tai cua nguoi dan
	private boolean conSong = true;

	// Quan he cua nguoi dan voi chu ho trong ho gia dinh
	private QuanHeChuHo quanHeChuHo;

	// Khoi tao doi tuong nguoi dan voi CCCD va thong tin ca nhan
	public NguoiDan(String CCCD, String hoTen, GioiTinh gioiTinh, LocalDate ngaySinh, TinhTrangHonNhan tinhTrangHonNhan,
			QuanHeChuHo quanHeChuHo) {

		// Kiem tra CCCD truoc khi khoi tao doi tuong
		if (CCCD == null || CCCD.trim().isEmpty()) {
			throw new IllegalArgumentException("CCCD khong hop le");
		}

		this.CCCD = CCCD;

		// Su dung phuong thuc cap nhat de kiem tra va gan thong tin ca nhan
		capNhatThongTin(hoTen, gioiTinh, ngaySinh, tinhTrangHonNhan, quanHeChuHo);
	}

	// Cap nhat thong tin ca nhan cua nguoi dan
	public void capNhatThongTin(String hoTen, GioiTinh gioiTinh, LocalDate ngaySinh, TinhTrangHonNhan tinhTrangHonNhan,
			QuanHeChuHo quanHeChuHo) {

		// Kiem tra cac thong tin bat buoc khong duoc de trong
		if (hoTen == null || hoTen.trim().isEmpty() || gioiTinh == null || ngaySinh == null || tinhTrangHonNhan == null
				|| quanHeChuHo == null) {

			throw new IllegalArgumentException("Thong tin nguoi dan khong hop le");
		}

		// Ngay sinh khong duoc nam trong tuong lai
		if (ngaySinh.isAfter(LocalDate.now())) {
			throw new IllegalArgumentException("Ngay sinh khong hop le");
		}

		// Gan cac thong tin sau khi da kiem tra hop le
		this.hoTen = hoTen;
		this.gioiTinh = gioiTinh;
		this.ngaySinh = ngaySinh;
		this.tinhTrangHonNhan = tinhTrangHonNhan;
		this.quanHeChuHo = quanHeChuHo;
	}

	// Lay CCCD cua nguoi dan
	public String getCCCD() {
		return CCCD;
	}

	// Lay ho ten cua nguoi dan
	public String getHoTen() {
		return hoTen;
	}

	// Lay gioi tinh cua nguoi dan
	public GioiTinh getGioiTinh() {
		return gioiTinh;
	}

	// Lay ngay sinh cua nguoi dan
	public LocalDate getNgaySinh() {
		return ngaySinh;
	}

	// Lay tinh trang hon nhan cua nguoi dan
	public TinhTrangHonNhan getTinhTrangHonNhan() {
		return tinhTrangHonNhan;
	}

	// Lay quan he cua nguoi dan voi chu ho
	public QuanHeChuHo getQuanHeChuHo() {
		return quanHeChuHo;
	}

	// Kiem tra tinh trang song hien tai
	public boolean isConSong() {
		return conSong;
	}

	// Cap nhat tinh trang song hien tai cua nguoi dan
	public void capNhatConSong(boolean conSong) {
		this.conSong = conSong;
	}

}
