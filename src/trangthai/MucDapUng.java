package trangthai;

// Mức đáp ứng hỗ trợ so với nhu cầu (dùng cho hộ dân, khu vực, địa phương)
public enum MucDapUng {
    CHUA_HO_TRO,    // nhu cầu > 0 nhưng chưa hỗ trợ đồng nào
    MOT_PHAN,       // đã hỗ trợ nhưng chưa đủ nhu cầu
    DAY_DU;         // đã hỗ trợ đủ (hoặc không có nhu cầu)

    /**
     * Chấm mức đáp ứng:
     *  - nhu cầu = 0 (không cần hỗ trợ)        -> DAY_DU
     *  - đã hỗ trợ = 0                          -> CHUA_HO_TRO
     *  - đã hỗ trợ < nhu cầu                    -> MOT_PHAN
     *  - còn lại (đã hỗ trợ >= nhu cầu)         -> DAY_DU
     * Ném IllegalArgumentException nếu một trong hai số âm.
     */
    public static MucDapUng danhGia(long nhuCau, long daHoTro) {
        if (nhuCau < 0 || daHoTro < 0) {
            throw new IllegalArgumentException("Nhu cau va so tien da ho tro khong duoc am");
        }
        if (nhuCau == 0) {
            return DAY_DU;
        }
        if (daHoTro == 0) {
            return CHUA_HO_TRO;
        }
        if (daHoTro < nhuCau) {
            return MOT_PHAN;
        }
        return DAY_DU;
    }
}
