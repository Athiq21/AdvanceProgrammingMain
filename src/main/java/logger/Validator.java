package logger;

import Interface.SubCategoryServiceImpl;
import services.SubCategoryService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Validator implements SubCategoryServiceImpl {
    private SubCategoryServiceImpl subCategoryService;

    protected SubCategoryServiceImpl wrappedService;


    public Validator(SubCategoryServiceImpl subCategoryService) {
        this.subCategoryService = subCategoryService;
    }

    private void validateRequestMethod(HttpServletRequest req, HttpServletResponse resp, String expectedMethod) throws IOException {
        if (!req.getMethod().equals(expectedMethod)) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Invalid request method. Expected: " + expectedMethod);
        }
    }

    @Override
    public void getSubCategoriesByCategoryId(HttpServletRequest req, HttpServletResponse resp, int categoryId) throws IOException {
        validateRequestMethod(req, resp, "GET");
        subCategoryService.getSubCategoriesByCategoryId(req, resp,categoryId);
    }

    @Override
    public void getAllSubCategory(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        validateRequestMethod(req, resp, "GET"); // Validate request method
        subCategoryService.getAllSubCategory(req, resp); // Delegate to the wrapped service
    }

    @Override
    public void addSubCategory(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getMethod().equals("POST")) {
            subCategoryService.addSubCategory(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Invalid request method");
        }
    }

    @Override
    public void updateSubCategory(HttpServletRequest req, HttpServletResponse resp, String id) throws IOException {
        if (req.getMethod().equals("PUT")) {
            subCategoryService.updateSubCategory(req, resp, id);
        } else {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Invalid request method");
        }
    }

    @Override
    public void deleteSubCategory(HttpServletRequest req, HttpServletResponse resp, String id) throws IOException {
        // Add validation logic here
        if (req.getMethod().equals("DELETE")) {
            subCategoryService.deleteSubCategory(req, resp, id);
        } else {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Invalid request method");
        }
    }
}
