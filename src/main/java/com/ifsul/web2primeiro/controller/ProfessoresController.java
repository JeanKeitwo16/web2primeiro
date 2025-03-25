package com.ifsul.web2primeiro.controller;

import java.lang.ProcessBuilder.Redirect;

import javax.naming.Binding;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ifsul.web2primeiro.dtos.ProfessorDTO;
import com.ifsul.web2primeiro.model.Professores;
import com.ifsul.web2primeiro.repository.ProfessoresRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/professores")
public class ProfessoresController {
    @Autowired
    ProfessoresRepository repository;

    @GetMapping("/inserir")
    public String inserir() {
        return "professores/inserir";
    }

    @PostMapping("/inserir")
    public String inserido(
            @ModelAttribute @Valid ProfessorDTO dto,
            BindingResult result, RedirectAttributes msg) {
        if (result.hasErrors()) {
            msg.addFlashAttribute("Erro ao Inserir");
            return "redirect:/professores/inserir";
        }
        var professores = new Professores();
        BeanUtils.copyProperties(dto, professores);
        repository.save(professores);
        msg.addFlashAttribute("SucessoCadastrar", "Professor cadastrado com sucesso!");
        return "redirect:/professores/inserir";
    }

    @GetMapping("/listar")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("professores/listar");
        List<Professores> lista = repository.findAll();
        mv.addObject("professores", lista);
        return mv;
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable(value = "id") int id) {
        Optional<Professores> professores = repository.findById(id);
        if (professores.isEmpty()) {
            return "redirect:/professores/listar";
        }
        repository.deleteById(id);
        return "redirect:/professores/listar";
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable(value = "id") int id) {
        ModelAndView mv = new ModelAndView("professores/editar");
        Optional<Professores> professores = repository.findById(id);
        mv.addObject("id", professores.get().getID());
        mv.addObject("nome", professores.get().getNome());
        return mv;
    }

    @PostMapping("/editar/{id}")
    public String editado(
            @ModelAttribute @Valid ProfessorDTO dto,
            BindingResult result, RedirectAttributes msg,
            @PathVariable(value = "id") int id) {
        if (result.hasErrors()) {
            msg.addFlashAttribute("Erro ao Inserir");
            return "redirect:professores/listar";
        }
        Optional<Professores> prof = repository.findById(id);
        var professores = prof.get();
        BeanUtils.copyProperties(dto, professores);
        repository.save(professores);
        msg.addFlashAttribute("SucessoCadastrar", "Professor cadastrado com sucesso!");
        return "redirect:/professores/listar";
    }

}
