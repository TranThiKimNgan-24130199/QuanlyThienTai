package main;

public class ThietHaiCongTrinhCong extends ThietHaiCoSoVatChatNN {
    private int soCongTrinhBiHuHong;
    private int soCongTrinhBiSapDo;

    public ThietHaiCongTrinhCong(String maCSVC) { 
    	super(maCSVC, "CONG_TRINH_CONG"); 
    	}

    public void capNhatCongTrinh(int soCongTrinhBiHuHong, int soCongTrinhBiSapDo, double tongGiaTriThietHai) {
        this.soCongTrinhBiHuHong = soCongTrinhBiHuHong;
        this.soCongTrinhBiSapDo = soCongTrinhBiSapDo;
        capNhatGiaTriThietHai(tongGiaTriThietHai);
    }

    @Override 
    public double tinhTongThietHai() { 
    	return getTongGiaTriThietHai(); 
    	}

    @Override 
    public boolean kiemTraHopLe() {
//        return super.kiemTraHopLe() && soCongTrinhBiHuHong >= 0 && soCongTrinhBiSapDo >= 0;
    	return false;
    }

	public int getSoCongTrinhBiHuHong() {
		return soCongTrinhBiHuHong;
	}

	public int getSoCongTrinhBiSapDo() {
		return soCongTrinhBiSapDo;
	}

   
}
