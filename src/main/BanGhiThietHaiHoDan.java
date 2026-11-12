
package main;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import trangthai.CapDanhGia;
import trangthai.MucDoThietHai;
import trangthai.TrangThaiNguoi;

// Ban ghi thiet hai cua mot ho dan trong mot thien tai
// (gom thiet hai tai san va trang thai thiet hai cua tung thanh vien ho)
public class BanGhiThietHaiHoDan {

    private String maBanGhi;
    private LocalDateTime thoiGianGhiNhan;
    private HoDan hoDan;

    // Luu trang thai thiet hai cua tung thanh vien theo CCCD
    private HashMap<String, TrangThaiNguoi> dsTrangThaiNguoi = new HashMap<>();

    // Danh sach thiet hai tai san cua ho dan
    private ArrayList<ThietHaiTaiSanDan> dsThietHaiTaiSan = new ArrayList<>();

    // Tao ban ghi cho mot ho dan voi ma va thoi diem ghi nhan
    public BanGhiThietHaiHoDan(String maBanGhi, LocalDateTime thoiGianGhiNhan, HoDan hoDan) {

        if (maBanGhi == null || maBanGhi.trim().isEmpty()
                || thoiGianGhiNhan == null || hoDan == null) {
            throw new IllegalArgumentException("Thong tin ban ghi khong hop le");
        }

        this.maBanGhi = maBanGhi;
        this.thoiGianGhiNhan = thoiGianGhiNhan;
        this.hoDan = hoDan;

        // Mac dinh moi thanh vien trong ho co trang thai binh thuong
        for (NguoiDan nd : hoDan.getDsThanhVien()) {
            dsTrangThaiNguoi.put(nd.getCCCD(), TrangThaiNguoi.BINH_THUONG);
        }
    }

    // Doi thoi diem ghi nhan cua ban ghi
    public void capNhatThoiGianGhiNhan(LocalDateTime thoiGian) {

        if (thoiGian == null) {
            throw new IllegalArgumentException("Thoi gian ghi nhan khong hop le");
        }

        this.thoiGianGhiNhan = thoiGian;
    }

    // Cap nhat trang thai thiet hai cua mot thanh vien trong ho
    public boolean capNhatTrangThaiNguoi(String cccd, TrangThaiNguoi trangThai) {

        if (cccd == null || cccd.trim().isEmpty() || trangThai == null) {
            return false;
        }

        // Chi cap nhat trang thai cho nguoi thuoc ho dan nay
        if (hoDan.timThanhVien(cccd) == null) {
            return false;
        }

        // Dong bo tinh trang song cua nguoi dan voi trang thai thiet hai:
        // ghi TU_VONG thi khong con song; sua lai tu TU_VONG sang trang thai khac thi con song tro lai
        NguoiDan nguoi = hoDan.timThanhVien(cccd);
        TrangThaiNguoi trangThaiCu = dsTrangThaiNguoi.get(cccd);
        if (trangThai == TrangThaiNguoi.TU_VONG) {
            nguoi.capNhatConSong(false);
        } else if (trangThaiCu == TrangThaiNguoi.TU_VONG) {
            nguoi.capNhatConSong(true);
        }
        dsTrangThaiNguoi.put(cccd, trangThai);
        return true;
    }

    /*
     * Them mot thiet hai tai san vao ban ghi. Tra ve false neu thiet hai null, khong hop le,
     * hoac ban ghi da co thiet hai cung loai (moi loai nha cua / san xuat chi co toi da mot muc).
     */
    public boolean capNhatThietHaiTaiSan(ThietHaiTaiSanDan thietHai) {

        if (thietHai == null || !thietHai.kiemTraHopLe()) {
            return false;
        }

        for (ThietHaiTaiSanDan t : dsThietHaiTaiSan) {
            if (t.getClass() == thietHai.getClass()) {
                return false;
            }
        }

        return dsThietHaiTaiSan.add(thietHai);
    }

    // Kiem tra ban ghi hop le: co ma, thoi gian, ho dan va cac du lieu ben trong hop le
    public boolean kiemTraHopLe() {

        if (maBanGhi == null || maBanGhi.trim().isEmpty()
                || thoiGianGhiNhan == null || hoDan == null) {
            return false;
        }

        for (String cccd : dsTrangThaiNguoi.keySet()) {
            if (cccd == null || cccd.trim().isEmpty()
                    || dsTrangThaiNguoi.get(cccd) == null) {
                return false;
            }
        }

        for (ThietHaiTaiSanDan t : dsThietHaiTaiSan) {
            if (t == null || !t.kiemTraHopLe()) {
                return false;
            }
        }

        return true;
    }

    /*
     * Tinh thong ke rieng cua ho:
     * Dem so thanh vien chet / mat tich / bi thuong theo trang thai trong ban ghi,
     * roi cong gia tri tung thiet hai tai san vao thong ke.
     */
    public ThongKeThietHai tinhThongKe() {

        ThongKeThietHai tk = new ThongKeThietHai();

        int soNguoiChet = 0;
        int soNguoiMatTich = 0;
        int soNguoiBiThuong = 0;

        for (TrangThaiNguoi trangThai : dsTrangThaiNguoi.values()) {

            if (trangThai == TrangThaiNguoi.TU_VONG) {
                soNguoiChet++;
            } else if (trangThai == TrangThaiNguoi.MAT_TICH) {
                soNguoiMatTich++;
            } else if (trangThai == TrangThaiNguoi.BI_THUONG) {
                soNguoiBiThuong++;
            }
        }

        tk.capNhatSoNguoi(soNguoiChet, soNguoiMatTich, soNguoiBiThuong);

        for (ThietHaiTaiSanDan t : dsThietHaiTaiSan) {
            t.capNhatThongTinThongKe(tk);
        }

        return tk;
    }

    // Danh gia muc do thiet hai cua ho theo bang nguong cap HO_DAN
    public MucDoThietHai danhGiaMucDo() {
        return tinhThongKe().danhGiaMucDo(CapDanhGia.HO_DAN);
    }

    // Cac phuong thuc getter
    public String getMaBanGhi() {
        return maBanGhi;
    }

    public LocalDateTime getThoiGianGhiNhan() {
        return thoiGianGhiNhan;
    }

    public HoDan getHoDan() {
        return hoDan;
    }

    // Tra ve ban sao danh sach trang thai nguoi
    public HashMap<String, TrangThaiNguoi> getDsTrangThaiNguoi() {
        return new HashMap<>(dsTrangThaiNguoi);
    }

    // Tra ve ban sao danh sach thiet hai tai san
    public ArrayList<ThietHaiTaiSanDan> getDsThietHaiTaiSan() {
        return new ArrayList<>(dsThietHaiTaiSan);
    }
}
