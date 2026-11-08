package main;

public class ThietHaiCauDuong extends ThietHaiCoSoVatChatNN {
    private int soCauBiHuHong;
    private int soCauBiSap;
    private double chieuDaiDuongBiHuHong;

    public ThietHaiCauDuong(String maCSVC) { 
    	super(maCSVC, "CAU_DUONG"); 
    	}

    public void capNhatCauDuong(int soCauBiHuHong, int soCauBiSap,
                                double chieuDaiDuongBiHuHong, double tongGiaTriThietHai) {
        this.soCauBiHuHong = soCauBiHuHong;
        this.soCauBiSap = soCauBiSap;
        this.chieuDaiDuongBiHuHong = chieuDaiDuongBiHuHong;
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

	public int getSoCauBiHuHong() {
		return soCauBiHuHong;
	}

	public int getSoCauBiSap() {
		return soCauBiSap;
	}

	public double getChieuDaiDuongBiHuHong() {
		return chieuDaiDuongBiHuHong;
	}

  
}

