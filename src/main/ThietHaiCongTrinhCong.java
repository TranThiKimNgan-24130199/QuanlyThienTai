
package main;
//người thực hiện: Nguyễn Thị Hoài Lan
//số công trình bị sập được tính riêng, không cộng trùng vào số công trình bị hư hỏng.
//Ví dụ, 3 công trình hư hỏng và 2 công trình sập nghĩa là tổng cộng 5 công trình bị ảnh hưởng.

public class ThietHaiCongTrinhCong extends ThietHaiCoSoVatChat_NN {

    private int soCongTrinhBiHuHong;
    private int soCongTrinhBiSapDo;

    public ThietHaiCongTrinhCong(String maCSVC, String maTT) {
        super(maCSVC, "CONG_TRINH_CONG", maTT);
    }

    // Cập nhật số lượng công trình công bị thiệt hại
    // và tổng giá trị thiệt hại
    public void capNhatCongTrinh(int soCongTrinhBiHuHong,
                                 int soCongTrinhBiSapDo,
                                 long tongGiaTriThietHai) {

        if (soCongTrinhBiHuHong < 0 || soCongTrinhBiSapDo < 0
                || tongGiaTriThietHai < 0) {
            throw new IllegalArgumentException("Du lieu thiet hai khong hop le");
        }

        this.soCongTrinhBiHuHong = soCongTrinhBiHuHong;
        this.soCongTrinhBiSapDo = soCongTrinhBiSapDo;

        capNhatGiaTriThietHai(tongGiaTriThietHai);
    }

    @Override
    public void capNhatThongTinThongKe(ThongKeThietHai tk) {
        if (tk == null) return;

        tk.themGiaTriCongTrinhCong(tinhTongThietHai());
    }

    @Override
    public boolean kiemTraHopLe() {
        return super.kiemTraHopLe()
                && soCongTrinhBiHuHong >= 0
                && soCongTrinhBiSapDo >= 0;
    }

    public int getSoCongTrinhBiHuHong() {
        return soCongTrinhBiHuHong;
    }

    public int getSoCongTrinhBiSapDo() {
        return soCongTrinhBiSapDo;
    }
}
