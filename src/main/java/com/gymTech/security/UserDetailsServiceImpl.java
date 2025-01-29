package com.gymTech.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gymTech.model.Aluno;
import com.gymTech.repository.AlunoRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private AlunoRepository alunoRepository;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

		Optional<Aluno> aluno = alunoRepository.findByUsuario(userName);
		

		if (aluno.isPresent())
			return new UserDetailsImpl(aluno.get());
		else
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
			
	}
}
