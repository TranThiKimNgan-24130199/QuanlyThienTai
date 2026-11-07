package main;

public class ThietHaiSanXuat extends ThietHaiVatChat {
    private double dienTichCayTrongBiThietHai;
    private int soVatNuoiBiThietHai;

    public ThietHaiSanXuat(String maCSVC) { 
    	super(maCSVC, "SAN_XUAT"); 
    	}

    public void capNhatSanXuat(double dienTichCayTrongBiThietHai, int soVatNuoiBiThietHai,
                               double tongGiaTriThietHai) {
        this.dienTichCayTrongBiThietHai = dienTichCayTrongBiThietHai;
        this.soVatNuoiBiThietHai = soVatNuoiBiThietHai;
        capNhatGiaTriThietHai(tongGiaTriThietHai);
    }

    @Override 
    public double tinhTongThietHai() { 
    	return getTongGiaTriThietHai(); 
    	}

    @Override 
    public boolean kiemTraHopLe() {
    	return false;
    }

	public double getDienTichCayTrongBiThietHai() {
		return dienTichCayTrongBiThietHai;
	}

	public int getSoVatNuoiBiThietHai() {
		return soVatNuoiBiThietHai;
	}

   
}

