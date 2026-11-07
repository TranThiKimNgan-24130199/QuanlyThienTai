package main;

public class ThietHaiVeCua extends ThietHaiVatChat {
    private int soNhaSapHoanToan;
    private int soNhaHuHong;

    public ThietHaiVeCua(String maCSVC) { 
    	super(maCSVC, "NHA_CUA"); 
    	}

    public void capNhatVeCua(int soNhaSapHoanToan, int soNhaHuHong, double tongGiaTriThietHai) {
        this.soNhaSapHoanToan = soNhaSapHoanToan;
        this.soNhaHuHong = soNhaHuHong;
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

	public int getSoNhaSapHoanToan() {
		return soNhaSapHoanToan;
	}

	public int getSoNhaHuHong() {
		return soNhaHuHong;
	}

}
