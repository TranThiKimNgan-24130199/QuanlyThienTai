package trangthai;

//Mức độ đóng góp của một nguồn kinh phí (không áp dụng cho nguồn Nhà nước)
public enum MucDoDongGop {
	THAP,
	TRUNG_BINH,
	CAO,
	RAT_CAO;

	//Ngưỡng (VNĐ) theo nhóm: dưới ngưỡng[0] là THAP, dưới ngưỡng[1] là TRUNG_BINH, dưới ngưỡng[2] là CAO, còn lại RAT_CAO
	private static final long[] NGUONG_CA_NHAN = {1_000_000L, 5_000_000L, 20_000_000L};
	private static final long[] NGUONG_TAP_THE = {10_000_000L, 50_000_000L, 200_000_000L};
	private static final long[] NGUONG_CONG_TY = {50_000_000L, 200_000_000L, 1_000_000_000L};

	/*
	 * Phân loại mức độ đóng góp theo nhóm nguồn và tổng số tiền đã đóng góp
	 * Trả về null nếu nhóm null, là NHA_NUOC (không phân mức độ) hoặc số tiền âm
	 */
	public static MucDoDongGop phanLoai(NhomNguon nhom, long tongSoTien) {
		if (nhom == null || nhom == NhomNguon.NHA_NUOC || tongSoTien < 0) {
			return null;
		}
		long[] nguong;
		switch (nhom) {
			case CA_NHAN: nguong = NGUONG_CA_NHAN; break;
			case TAP_THE: nguong = NGUONG_TAP_THE; break;
			default:      nguong = NGUONG_CONG_TY; break;
		}
		if (tongSoTien < nguong[0]) return THAP;
		if (tongSoTien < nguong[1]) return TRUNG_BINH;
		if (tongSoTien < nguong[2]) return CAO;
		return RAT_CAO;
	}
}
