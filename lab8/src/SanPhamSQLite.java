
import java.sql.*;
import java.util.Scanner;

public class SanPhamSQLite {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        String url = "jdbc:sqlite:nhanvien.db";
        Connection conn = null;
        Statement stmt = null;

        try {
            // Ket noi SQLite
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);

            System.out.println("Ket noi SQLite thanh cong!");

            // Tao bang neu chua ton tai
            stmt = conn.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS SanPham (" +
                    "maSP INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "tenSP TEXT NOT NULL, " +
                    "gia REAL, " +
                    "soluong INTEGER)";

            stmt.executeUpdate(sql);
            stmt.close();
            stmt = null;

            int chon;

            do {
                System.out.println("\n========== MENU ==========");
                System.out.println("1. Them san pham");
                System.out.println("2. Hien thi danh sach");
                System.out.println("3. Tim kiem san pham");
                System.out.println("4. Cap nhat san pham");
                System.out.println("5. Xoa san pham");
                System.out.println("6. Thoat");
                System.out.println("==========================");

                System.out.print("Nhap lua chon: ");
                chon = Integer.parseInt(sc.nextLine());

                switch (chon) {

                    case 1:
                        themSanPham(conn);
                        break;

                    case 2:
                        hienThiSanPham(conn);
                        break;

                    case 3:
                        timKiemSanPham(conn);
                        break;

                    case 4:
                        capNhatSanPham(conn);
                        break;

                    case 5:
                        xoaSanPham(conn);
                        break;

                    case 6:
                        System.out.println("Thoat chuong trinh!");
                        break;

                    default:
                        System.out.println("Lua chon khong hop le!");
                }

            } while (chon != 6);

        } catch (ClassNotFoundException e) {

            System.out.println("Khong tim thay JDBC Driver!");
            e.printStackTrace();

        } catch (SQLException e) {

            System.out.println("Loi SQL!");
            e.printStackTrace();

        } catch (NumberFormatException e) {

            System.out.println("Vui long nhap so hop le!");

        } finally {

            // Dong tai nguyen
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();

                System.out.println("Da dong ket noi!");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 1. THEM SAN PHAM
    public static void themSanPham(Connection conn)
            throws SQLException {

        System.out.println("\n--- THEM SAN PHAM ---");

        System.out.print("Nhap ten san pham: ");
        String ten = sc.nextLine();

        System.out.print("Nhap gia: ");
        double gia = Double.parseDouble(sc.nextLine());

        System.out.print("Nhap so luong: ");
        int soluong = Integer.parseInt(sc.nextLine());

        String sql =
                "INSERT INTO SanPham(tenSP, gia, soluong) " +
                "VALUES(?, ?, ?)";

        try (PreparedStatement pstmt =
                conn.prepareStatement(sql)) {

            pstmt.setString(1, ten);
            pstmt.setDouble(2, gia);
            pstmt.setInt(3, soluong);

            pstmt.executeUpdate();

            System.out.println("Them san pham thanh cong!");
        }
    }

    // 2. HIEN THI SAN PHAM
    public static void hienThiSanPham(Connection conn)
            throws SQLException {

        String sql = "SELECT * FROM SanPham";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- DANH SACH SAN PHAM ---");

            boolean coDuLieu = false;

            while (rs.next()) {

                coDuLieu = true;

                System.out.println(
                        "Ma SP: " + rs.getInt("maSP") +
                        " | Ten: " + rs.getString("tenSP") +
                        " | Gia: " + rs.getDouble("gia") +
                        " | So luong: " + rs.getInt("soluong")
                );
            }

            if (!coDuLieu) {
                System.out.println("Danh sach trong!");
            }
        }
    }

    // 3. TIM KIEM SAN PHAM
    public static void timKiemSanPham(Connection conn)
            throws SQLException {

        System.out.print("Nhap ten san pham can tim: ");
        String ten = sc.nextLine();

        String sql =
                "SELECT * FROM SanPham WHERE tenSP LIKE ?";

        try (PreparedStatement pstmt =
                conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + ten + "%");

            try (ResultSet rs = pstmt.executeQuery()) {

                System.out.println("\n--- KET QUA TIM KIEM ---");

                boolean coDuLieu = false;

                while (rs.next()) {

                    coDuLieu = true;

                    System.out.println(
                            "Ma SP: " + rs.getInt("maSP") +
                            " | Ten: " + rs.getString("tenSP") +
                            " | Gia: " + rs.getDouble("gia") +
                            " | So luong: " + rs.getInt("soluong")
                    );
                }

                if (!coDuLieu) {
                    System.out.println("Khong tim thay san pham!");
                }
            }
        }
    }

    // 4. CAP NHAT SAN PHAM
    public static void capNhatSanPham(Connection conn)
            throws SQLException {

        System.out.print("Nhap ma san pham can sua: ");
        int maSP = Integer.parseInt(sc.nextLine());

        System.out.print("Nhap gia moi: ");
        double gia = Double.parseDouble(sc.nextLine());

        System.out.print("Nhap so luong moi: ");
        int soluong = Integer.parseInt(sc.nextLine());

        String sql =
                "UPDATE SanPham SET gia = ?, soluong = ? " +
                "WHERE maSP = ?";

        try (PreparedStatement pstmt =
                conn.prepareStatement(sql)) {

            pstmt.setDouble(1, gia);
            pstmt.setInt(2, soluong);
            pstmt.setInt(3, maSP);

            int result = pstmt.executeUpdate();

            if (result > 0) {
                System.out.println("Cap nhat thanh cong!");
            } else {
                System.out.println("Khong tim thay ma san pham!");
            }
        }
    }

    // 5. XOA SAN PHAM
    public static void xoaSanPham(Connection conn)
            throws SQLException {

        System.out.print("Nhap ma san pham can xoa: ");
        int maSP = Integer.parseInt(sc.nextLine());

        String sql =
                "DELETE FROM SanPham WHERE maSP = ?";

        try (PreparedStatement pstmt =
                conn.prepareStatement(sql)) {

            pstmt.setInt(1, maSP);

            int result = pstmt.executeUpdate();

            if (result > 0) {
                System.out.println("Xoa san pham thanh cong!");
            } else {
                System.out.println("Khong tim thay ma san pham!");
            }
        }
    }
}
