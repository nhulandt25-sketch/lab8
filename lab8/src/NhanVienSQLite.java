
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NhanVienSQLite {

    public static void main(String[] args) {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // B1: Ket noi SQLite
            Class.forName("org.sqlite.JDBC");

            String url = "jdbc:sqlite:nhanvien.db";

            conn = DriverManager.getConnection(url);

            System.out.println("Ket noi SQLite thanh cong!");

            // B2: Tao Statement
            stmt = conn.createStatement();

            // B3.1: Tao bang NhanVien
            String sql_create_table =
                "CREATE TABLE IF NOT EXISTS NhanVien (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ten TEXT NOT NULL, " +
                "chuc_vu TEXT" +
                ")";

            stmt.executeUpdate(sql_create_table);

            System.out.println("Tao bang thanh cong!");

            // B3.2: Them 3 nhan vien
            String sql_insert1 =
                "INSERT INTO NhanVien(ten, chuc_vu) " +
                "VALUES('Nguyen Van An', 'Ke toan')";

            String sql_insert2 =
                "INSERT INTO NhanVien(ten, chuc_vu) " +
                "VALUES('Tran Van Binh', 'Nhan vien')";

            String sql_insert3 =
                "INSERT INTO NhanVien(ten, chuc_vu) " +
                "VALUES('Le Thi Hoa', 'Quan ly')";

            stmt.executeUpdate(sql_insert1);
            stmt.executeUpdate(sql_insert2);
            stmt.executeUpdate(sql_insert3);

            System.out.println("Them 3 nhan vien thanh cong!");

            // B4: Truy van nhan vien
            String sql_select =
                "SELECT id, ten, chuc_vu FROM NhanVien";

            rs = stmt.executeQuery(sql_select);

            System.out.println("\n===== DANH SACH NHAN VIEN =====");

            while (rs.next()) {

                int id = rs.getInt("id");
                String ten = rs.getString("ten");
                String chucVu = rs.getString("chuc_vu");

                System.out.println(
                    "ID: " + id +
                    " | Ten: " + ten +
                    " | Chuc vu: " + chucVu
                );
            }

        } catch (ClassNotFoundException e) {

            System.out.println("Khong tim thay JDBC Driver!");
            e.printStackTrace();

        } catch (SQLException e) {

            System.out.println("Loi SQL!");
            e.printStackTrace();

        } finally {

            // B5: Dong tai nguyen
            try {
                if (rs != null) {
                    rs.close();
                }

                if (stmt != null) {
                    stmt.close();
                }

                if (conn != null) {
                    conn.close();
                }

                System.out.println("\nDa dong ket noi!");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
