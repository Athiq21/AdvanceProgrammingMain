package servlet.Appinit;

import services.RoleService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet(name = "AppInitServlet", urlPatterns = {}, loadOnStartup = 1)
public class AppInitServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("asdfhasdhasdasd");

        RoleService roleService = new RoleService();
        roleService.insertRolesIntoDatabase();

        System.out.println("Role Add");
    }
}

