package main;

public abstract class ThietHaiVatChat {
    private String maCSVC;
    private String loaiCSVC;
    private double tongGiaTriThietHai;  

    protected ThietHaiVatChat(String maCSVC, String loaiCSVC) {
        this.maCSVC = maCSVC;
        this.loaiCSVC = loaiCSVC;
    }

    public double tinhTongThietHai() { 
    	return tongGiaTriThietHai; 
    	}

    public void capNhatGiaTriThietHai(double soTien) { 
    	this.tongGiaTriThietHai = soTien; 
    	}

    public boolean kiemTraHopLe() { 
    	return tongGiaTriThietHai >= 0; 
    	}

	public String getMaCSVC() {
		return maCSVC;
	}

	public String getLoaiCSVC() {
		return loaiCSVC;
	}

	public double getTongGiaTriThietHai() {
		return tongGiaTriThietHai;
	}

}
