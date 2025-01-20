package Interface;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface SubCategoryServiceImpl {
    void getAllSubCategory(HttpServletRequest req, HttpServletResponse resp) throws IOException;
    void addSubCategory(HttpServletRequest req, HttpServletResponse resp) throws IOException;
    void updateSubCategory(HttpServletRequest req, HttpServletResponse resp, String id) throws IOException;
    void deleteSubCategory(HttpServletRequest req, HttpServletResponse resp, String id) throws IOException;
}
