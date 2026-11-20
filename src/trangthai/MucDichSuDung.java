package trangthai;

// Mục đích sử dụng của một nguồn kinh phí: người đóng góp chỉ định lúc góp, sau đó không đổi
public enum MucDichSuDung {
    KHONG_CHI_DINH,     // mặc định: chi cho việc nào cũng được
    HO_TRO_NGUOI_DAN,   // chỉ chi cho hộ dân
    CO_SO_HA_TANG       // chỉ chi cho hạ tầng (sửa chữa, xây mới)
}
