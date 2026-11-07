package main;

public class ThietHaiVeNguoi {
    private int soNguoiChet;
    private int soNguoiMatTich;
    private int soNguoiBiThuong;

    public ThietHaiVeNguoi() { }

    public ThietHaiVeNguoi(int soNguoiChet, int soNguoiMatTich, int soNguoiBiThuong) {
        this.soNguoiChet = soNguoiChet;
        this.soNguoiMatTich = soNguoiMatTich;
        this.soNguoiBiThuong = soNguoiBiThuong;
    }

    public void capNhatSoNguoi(int soNguoiChet, int soNguoiMatTich, int soNguoiBiThuong) {
        this.soNguoiChet = soNguoiChet;
        this.soNguoiMatTich = soNguoiMatTich;
        this.soNguoiBiThuong = soNguoiBiThuong;
    }

    public boolean kiemTraHopLe() {
    	return false;
    }

    public int tinhTongThietHai() { 
    	return soNguoiChet + soNguoiMatTich + soNguoiBiThuong; 
    	}

	public int getSoNguoiChet() {
		return soNguoiChet;
	}

	public int getSoNguoiMatTich() {
		return soNguoiMatTich;
	}

	public int getSoNguoiBiThuong() {
		return soNguoiBiThuong;
	}

  
}

