//package API;
//
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import servlet.AuthServlet;
//import services.AuthService;
//import services.EmailService;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.io.StringWriter;
//
//import static org.mockito.Mockito.*;
//
//class AuthTest {
//
//    @Test
//    void testSignup() throws IOException {
//
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        HttpServletResponse response = mock(HttpServletResponse.class);
//
//        String jsonBody = "{\"email\":\"athiq.rashiq@gmail.com\", \"password\":\"Athiq12@\", \"firstName\":\"Athiq\", \"lastName\":\"AHmeeer\"}";
//        when(request.getReader()).thenReturn(new java.io.BufferedReader(new java.io.StringReader(jsonBody)));
//
//        AuthService authService = mock(AuthService.class);
//        AuthServlet servlet = new AuthServlet();
//        servlet.handleSignup(request, response);
//        verify(authService, times(1)).handleSignup("athiq.rashiq@gmail.com", "Athiq12@", "Athiq", "Ahmeer", response);
//    }
//
//    @Test
//    void testSignin() throws IOException {
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        HttpServletResponse response = mock(HttpServletResponse.class);
//
//        String jsonBody = "{\"email\":\"test@example.com\", \"password\":\"password123\"}";
//        when(request.getReader()).thenReturn(new java.io.BufferedReader(new java.io.StringReader(jsonBody)));
//
//        AuthService authService = mock(AuthService.class);
//
//        AuthServlet servlet = new AuthServlet();
//        servlet.handleSignin(request, response);
//
//        // Assert (you can mock AuthService behavior and assert the right actions were called)
//        verify(authService, times(1)).handleSignin("test@example.com", "password123", response);
//    }
//
//    @Test
//    void testOtpVerification() throws IOException {
//        // Arrange
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        HttpServletResponse response = mock(HttpServletResponse.class);
//
//        // Simulate the JSON body in the request
//        String jsonBody = "{\"email\":\"test@example.com\", \"otp\":\"123456\"}";
//        when(request.getReader()).thenReturn(new java.io.BufferedReader(new java.io.StringReader(jsonBody)));
//
//        // Mock AuthService to not perform real actions
//        AuthService authService = mock(AuthService.class);
//
//        // Call the servlet method
//        AuthServlet servlet = new AuthServlet();
//        servlet.handleOtpVerification(request, response);
//
//        // Assert (you can mock AuthService behavior and assert the right actions were called)
//        verify(authService, times(1)).handleOtpVerification("test@example.com", "123456", response);
//    }
//
//    @Test
//    void testOtpResend() throws IOException {
//        // Arrange
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        HttpServletResponse response = mock(HttpServletResponse.class);
//
//        // Simulate the JSON body in the request
//        String jsonBody = "{\"email\":\"athiq.rashiq@gmail.com\"}";
//        when(request.getReader()).thenReturn(new java.io.BufferedReader(new java.io.StringReader(jsonBody)));
//
//        // Mock AuthService to not perform real actions
//        AuthService authService = mock(AuthService.class);
//
//        // Call the servlet method
//        AuthServlet servlet = new AuthServlet();
//        servlet.handleOtpResend(request, response);
//
//        // Assert (you can mock AuthService behavior and assert the right actions were called)
//        verify(authService, times(1)).handleOTPResends("test@example.com", response);
//    }
//}
