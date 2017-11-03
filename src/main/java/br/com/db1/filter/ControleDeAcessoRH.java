package br.com.db1.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.db1.model.Usuario;

@WebFilter("/LoginRH/*")
public class ControleDeAcessoRH implements Filter{
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();

		Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
		
		if (usuarioLogado != null && usuarioLogado.getAdministrador() || req.getRequestURI().endsWith("telaLogin.xhtml")) {
			chain.doFilter(request, response);
		} else {
			HttpServletResponse res = (HttpServletResponse) response;
			res.sendRedirect("../telaLogin.xhtml");
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {

	}

	public void destroy() {

	}
}
