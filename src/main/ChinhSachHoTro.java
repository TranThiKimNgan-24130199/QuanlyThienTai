package main;
//người thực hiện: Nguyễn Thị Hoài Lan
import trangthai.MucDoThietHai;
import trangthai.TrangThaiNguoi;

/*
 * ChinhSachHoTro: BẢNG MỨC HỖ TRỢ - quy định mỗi loại thiệt hại được hỗ trợ bao nhiêu.
 * Lớp này chỉ chứa hằng số và các hàm tính (static), không có đối tượng nào cả.
 *
 * 1) HỘ DÂN: tính theo TỪNG KHOẢN thiệt hại (giống cách chính sách thực tế quy định):
 *    mỗi khoản nhân số lượng rồi cộng lại thành "nhu cầu hỗ trợ" của hộ.
 *      - người chết hoặc mất tích : 25 triệu / người
 *      - người bị thương          :  5 triệu / người
 *      - nhà sập hoàn toàn        : 50 triệu / nhà
 *      - nhà hư hỏng              : 25 triệu / nhà
 *    Các mức này tham khảo MỨC TỐI THIỂU thực tế từ nguồn đóng góp tự nguyện
 *    (Ban Vận động Cứu trợ Trung ương, bão số 10, theo báo chí). Không phải quy định chính thức.
 *    Muốn đổi số thì chỉ sửa bốn hằng số ngay dưới, không phải sửa chỗ nào khác.
 *
 * 2) HẠ TẦNG (khu vực, địa phương): nhu cầu = giá trị thiệt hại hạ tầng x tỷ lệ theo mức thiệt hại.
 *    Thực tế hạ tầng công do ngân sách sửa theo giá trị thiệt hại; tỷ lệ này chỉ là cách mô hình
 *    hóa đơn giản do nhóm tự đặt.
 *
 * MucDoThietHai của riêng một hộ KHÔNG dùng để tính tiền hộ, chỉ để xếp loại và thống kê.
 */
public class ChinhSachHoTro {

    // ===== Mức hỗ trợ hộ dân (VNĐ). Dùng long vì số tiền lớn, tránh tràn kiểu int. =====
    public static final long MUC_NGUOI_CHET_MAT_TICH = 25_000_000L;
    public static final long MUC_NGUOI_BI_THUONG = 5_000_000L;
    public static final long MUC_NHA_SAP_HOAN_TOAN = 50_000_000L;
    public static final long MUC_NHA_HU_HONG = 25_000_000L;

    /*
     * Tỷ lệ phần trăm hỗ trợ hạ tầng, xếp THEO THỨ TỰ KHAI BÁO của MucDoThietHai:
     * KHONG_DANG_KE, NHE, TRUNG_BINH, NANG, RAT_NANG.
     * Hàm tyLeHoTroHaTang dùng muc.ordinal() để lấy đúng phần tử, nên nếu đổi thứ tự
     * của enum MucDoThietHai thì mảng này phải đổi theo (cách làm giống MucDoDongGop).
     */
    private static final int[] TY_LE_HA_TANG = { 0, 10, 30, 50, 70 };

    // Lớp chỉ có hàm static nên không cho tạo đối tượng
    private ChinhSachHoTro() {
    }

    /*
     * Nhu cầu hỗ trợ của MỘT hộ, tính từ bản ghi thiệt hại của hộ đó.
     * Đếm số người theo trạng thái (lấy từ dsTrangThaiNguoi), số nhà sập và nhà hư hỏng
     * (lấy từ ThietHaiNhaCua trong danh sách thiệt hại tài sản), rồi nhân với mức tương ứng.
     * Ném IllegalArgumentException nếu bản ghi null.
     */
    public static long tienHoTroHo(BanGhiThietHaiHoDan banGhi) {

        if (banGhi == null) {
            throw new IllegalArgumentException("Ban ghi thiet hai khong hop le");
        }

        long soNguoiChetMatTich = 0;
        long soNguoiBiThuong = 0;
        for (TrangThaiNguoi tt : banGhi.getDsTrangThaiNguoi().values()) {
            if (tt == TrangThaiNguoi.TU_VONG || tt == TrangThaiNguoi.MAT_TICH) {
                soNguoiChetMatTich++;
            } else if (tt == TrangThaiNguoi.BI_THUONG) {
                soNguoiBiThuong++;
            }
            // BINH_THUONG: không có khoản hỗ trợ
        }

        long soNhaSap = 0;
        long soNhaHuHong = 0;
        for (ThietHaiTaiSanDan t : banGhi.getDsThietHaiTaiSan()) {
            // Chỉ ThietHaiNhaCua mới có số nhà sập / hư hỏng, nên phải kiểm tra loại rồi ép kiểu
            if (t instanceof ThietHaiNhaCua) {
                ThietHaiNhaCua nha = (ThietHaiNhaCua) t;
                soNhaSap += nha.getSoNhaSapHoanToan();
                soNhaHuHong += nha.getSoNhaHuHong();
            }
        }

        return soNguoiChetMatTich * MUC_NGUOI_CHET_MAT_TICH
                + soNguoiBiThuong * MUC_NGUOI_BI_THUONG
                + soNhaSap * MUC_NHA_SAP_HOAN_TOAN
                + soNhaHuHong * MUC_NHA_HU_HONG;
    }

    // Tỷ lệ phần trăm (0..100) hỗ trợ hạ tầng ứng với mức thiệt hại. Ném lỗi nếu mức null.
    public static int tyLeHoTroHaTang(MucDoThietHai mucDo) {

        if (mucDo == null) {
            throw new IllegalArgumentException("Muc do thiet hai khong hop le");
        }

        return TY_LE_HA_TANG[mucDo.ordinal()];
    }

    /*
     * Nhu cầu hỗ trợ hạ tầng = giá trị thiệt hại hạ tầng x tỷ lệ / 100.
     * Chia số nguyên nên cắt phần lẻ dưới 1 đồng (không đáng kể).
     * Ném IllegalArgumentException nếu mức null hoặc giá trị âm.
     */
    public static long tinhNhuCauHaTang(MucDoThietHai mucDo, long giaTriThietHaiHaTang) {

        if (giaTriThietHaiHaTang < 0) {
            throw new IllegalArgumentException("Gia tri thiet hai ha tang khong duoc am");
        }

        return giaTriThietHaiHaTang * tyLeHoTroHaTang(mucDo) / 100;
    }
}
