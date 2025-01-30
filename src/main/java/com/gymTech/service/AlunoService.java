package com.gymTech.service;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gymTech.model.Aluno;
import com.gymTech.model.AlunoLogin;
import com.gymTech.repository.AlunoRepository;
import com.gymTech.security.JwtService;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;
    
    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

	public Optional<Aluno> cadastrarUsuario(Aluno aluno) {

		if (alunoRepository.findByUsuario(aluno.getUsuario()).isPresent())
			return Optional.empty();

		aluno.setSenha(criptografarSenha(aluno.getSenha()));

		return Optional.of(alunoRepository.save(aluno));
	
	}

	public Optional<Aluno> atualizarUsuario(Aluno aluno) {
		
		if(alunoRepository.findById(aluno.getId()).isPresent()) {

			Optional<Aluno> buscaUsuario = alunoRepository.findByUsuario(aluno.getUsuario());

			if ( (buscaUsuario.isPresent()) && ( buscaUsuario.get().getId() != aluno.getId()))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!", null);

			aluno.setSenha(criptografarSenha(aluno.getSenha()));

			return Optional.ofNullable(alunoRepository.save(aluno));
			
		}

		return Optional.empty();
	
	}	

	public Optional<AlunoLogin> autenticarUsuario(Optional<AlunoLogin> alunoLogin) {
        
        // Gera o Objeto de autenticação
		var credenciais = new UsernamePasswordAuthenticationToken(alunoLogin.get().getUsuario(), alunoLogin.get().getSenha());
		
        // Autentica o Usuario
		Authentication authentication = authenticationManager.authenticate(credenciais);
        
        // Se a autenticação foi efetuada com sucesso
		if (authentication.isAuthenticated()) {

            // Busca os dados do usuário
			Optional<Aluno> aluno = alunoRepository.findByUsuario(alunoLogin.get().getUsuario());

            // Se o usuário foi encontrado
			if (aluno.isPresent()) {

                // Preenche o Objeto usuarioLogin com os dados encontrados 
				alunoLogin.get().setId(aluno.get().getId());
				alunoLogin.get().setNome(aluno.get().getNome());
				alunoLogin.get().setFoto(aluno.get().getFoto());
				alunoLogin.get().setToken(gerarToken(alunoLogin.get().getUsuario()));
				alunoLogin.get().setSenha("");
				
                 // Retorna o Objeto preenchido
			   return alunoLogin;
			
			}

        } 
            
		return Optional.empty();

    }

	private String criptografarSenha(String senha) {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder.encode(senha);

	}

	private String gerarToken(String usuario) {
		return "Bearer " + jwtService.generateToken(usuario);
	}
    

    public Optional<Aluno> calcularImc(Long id) {
        return alunoRepository.findById(id).map(aluno -> {
            if (aluno.getAltura() == null || aluno.getPeso() == null || aluno.getAltura() <= 0 || aluno.getPeso() <= 0) {
                throw new IllegalArgumentException("Altura e peso devem ser valores positivos e não nulos.");
            }
            double imc = aluno.getPeso() / (aluno.getAltura() * aluno.getAltura());
            aluno.setImc(imc);
            aluno.setCategoriaImc(definirCategoriaImc(imc));
            alunoRepository.save(aluno);
            return aluno;
        });
    }

    private String definirCategoriaImc(double imc) {
        if (imc < 18.5) {
            return "Baixo peso";
        } else if (imc < 24.9) {
            return "Peso normal";
        } else if (imc < 29.9) {
            return "Sobrepeso";
        } else if (imc < 34.9) {
            return "Obesidade grau 1";
        } else if (imc < 39.9) {
            return "Obesidade grau 2";
        } else {
            return "Obesidade grau 3";
        }
    }
    }

   

