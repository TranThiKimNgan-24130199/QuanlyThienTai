
package main;
//Người thực hiện: Nguyễn Thị Hoài Lan
public abstract class ThietHaiCoSoVatChat_NN extends ThietHaiVatChat {

    // Ma dinh danh cua tung co so vat chat Nha nuoc
    private String maCSVC;

    // Loai co so vat chat: CAU_DUONG, CONG_TRINH_CONG,...
    private String loaiCSVC;

    // Constructor
    protected ThietHaiCoSoVatChat_NN(String maCSVC, String loaiCSVC) {

        super();

        if (maCSVC == null || maCSVC.trim().isEmpty()) {
            throw new IllegalArgumentException("Ma co so vat chat khong hop le");
        }

        if (loaiCSVC == null || loaiCSVC.trim().isEmpty()) {
            throw new IllegalArgumentException("Loai co so vat chat khong hop le");
        }

        this.maCSVC = maCSVC;
        this.loaiCSVC = loaiCSVC;
    }

    // Kiem tra tinh hop le cua co so vat chat
    @Override
    public boolean kiemTraHopLe() {

        return maCSVC != null
                && !maCSVC.trim().isEmpty()
                && loaiCSVC != null
                && !loaiCSVC.trim().isEmpty()
                && super.kiemTraHopLe();
    }

    // Getter
    public String getMaCSVC() {
        return maCSVC;
    }

    public String getLoaiCSVC() {
        return loaiCSVC;
    }
}
