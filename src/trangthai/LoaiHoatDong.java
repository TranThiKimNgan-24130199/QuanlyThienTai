package trangthai;

//người thực hiện: Nguyễn Thị Hoài Lan

/*
* LoaiHoatDong: một hoạt động khắc phục thuộc loại nào.
*
*/
public enum LoaiHoatDong {

  HO_TRO_HO_DAN,      // hỗ trợ tiền cho một hộ dân
  SUA_CHUA_HA_TANG,   // sửa chữa hạ tầng bị hư (đường, cầu, công trình công)
  XAY_MOI_HA_TANG;    // xây mới hạ tầng thay cho phần bị hư (ví dụ làm đường mới)

  /*
   * Loại hoạt động này đòi nguồn tiền có mục đích gì.
   * KhacPhucThienTai.ghiNhanChi dùng hàm này để so với mucDichSuDung của nguồn:
   * nguồn KHONG_CHI_DINH thì chi loại nào cũng được, còn nguồn có mục đích cụ thể
   * thì mục đích phải trùng với giá trị trả về ở đây.
   */
  public MucDichSuDung mucDichYeuCau() {
      if (this == HO_TRO_HO_DAN) {
          return MucDichSuDung.HO_TRO_NGUOI_DAN;
      }
      // hai loại còn lại đều là hạ tầng
      return MucDichSuDung.CO_SO_HA_TANG;
  }

}
