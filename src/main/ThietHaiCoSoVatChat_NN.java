
package main;
//người thực hiện: Nguyễn Thị Hoài Lan
public abstract class ThietHaiCoSoVatChat_NN extends ThietHaiVatChat {

    // Ma dinh danh cua tung co so vat chat Nha nuoc
    private String maCSVC;

    // Loai co so vat chat: CAU_DUONG, CONG_TRINH_CONG,...
    private String loaiCSVC;

    // Ma thien tai gay ra thiet hai nay, de moi thien tai chi tinh thiet hai cua chinh no
    private String maTT;

    // Constructor
    protected ThietHaiCoSoVatChat_NN(String maCSVC, String loaiCSVC, String maTT) {

        super();

        if (maCSVC == null || maCSVC.trim().isEmpty()) {
            throw new IllegalArgumentException("Ma co so vat chat khong hop le");
        }

        if (loaiCSVC == null || loaiCSVC.trim().isEmpty()) {
            throw new IllegalArgumentException("Loai co so vat chat khong hop le");
        }

        if (maTT == null || maTT.trim().isEmpty()) {
            throw new IllegalArgumentException("Ma thien tai khong hop le");
        }

        this.maCSVC = maCSVC;
        this.loaiCSVC = loaiCSVC;
        this.maTT = maTT;
    }

    // Kiem tra tinh hop le cua co so vat chat
    @Override
    public boolean kiemTraHopLe() {

        return maCSVC != null
                && !maCSVC.trim().isEmpty()
                && loaiCSVC != null
                && !loaiCSVC.trim().isEmpty()
                && maTT != null
                && !maTT.trim().isEmpty()
                && super.kiemTraHopLe();
    }

    // Getter
    public String getMaTT() {
        return maTT;
    }

    public String getMaCSVC() {
        return maCSVC;
    }

    public String getLoaiCSVC() {
        return loaiCSVC;
    }
}
