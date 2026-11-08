package main;
//So cau duong bi sap duoc tinh rieng, khong cong trung vao so cau duong bi hu hong.
//Vi du, 3 cau duong hu hong va 2 cau duong sap nghia la tong cong 5 doi tuong bi anh huong.
public class ThietHaiCauDuong extends ThietHaiCoSoVatChat_NN {

    private int soCauBiHuHong;
    private int soCauBiSap;

    public ThietHaiCauDuong(String maCSVC) {
        super(maCSVC, "CAU_DUONG");
    }

    // Cập nhật số lượng cầu đường bị thiệt hại và tổng giá trị thiệt hại
    public void capNhatCauDuong(int soCauBiHuHong,
                                int soCauBiSap,
                                double tongGiaTriThietHai) {

        if (soCauBiHuHong < 0 || soCauBiSap < 0
                || tongGiaTriThietHai < 0) {
            throw new IllegalArgumentException("Du lieu thiet hai khong hop le");
        }

        this.soCauBiHuHong = soCauBiHuHong;
        this.soCauBiSap = soCauBiSap;

        capNhatGiaTriThietHai(tongGiaTriThietHai);
    }

    @Override
    public double tinhTongThietHai() {
        return getTongGiaTriThietHai();
    }

    @Override
    public boolean kiemTraHopLe() {
        return super.kiemTraHopLe()
                && soCauBiHuHong >= 0
                && soCauBiSap >= 0;
    }

    public int getSoCauBiHuHong() {
        return soCauBiHuHong;
    }

    public int getSoCauBiSap() {
        return soCauBiSap;
    }
}