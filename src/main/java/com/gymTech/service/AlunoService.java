package com.gymTech.service;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gymTech.model.Aluno;
import com.gymTech.repository.AlunoRepository;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;
    
  
    

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

   

