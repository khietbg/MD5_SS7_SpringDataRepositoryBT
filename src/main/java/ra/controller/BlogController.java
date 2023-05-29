package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ra.model.entity.Blog;
import ra.model.entity.Catalog;
import ra.service.blog.IBlogService;
import ra.service.catalog.ICatalogService;

import java.time.LocalDate;
import java.util.Optional;

@Controller
public class BlogController {
    @Autowired
    private IBlogService blogService;
    @Autowired
    private ICatalogService catalogService;

    @ModelAttribute("catalog")
    Iterable<Catalog> getlist() {
        return catalogService.findAll();
    }

    @GetMapping("/")
    public String home(@RequestParam("sortBy") Optional<String> sortBy, ModelMap model, Pageable pageable) {
        Sort sort = null;
        if (sortBy.isPresent()) {
            switch (sortBy.get()) {
                case "title":
                    sort = Sort.by("title").ascending();
                    break;
                case "content":
                    sort = Sort.by("content").ascending();
                    break;
                default:
                    break;
            }

        } else {
            sort = Sort.by("date").ascending();
        }
        Page<Blog> listBlog = blogService.findAll(pageable, sort);

        model.addAttribute("list", listBlog);
        return "blog/blogList";
    }
    @GetMapping("/create")
    public ModelAndView add() {
        return new ModelAndView("blog/create", "blog", new Blog());
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("blog") Blog b) {
        b.setDate(LocalDate.now());
        blogService.save(b);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id) {
        Optional<Blog> blog = blogService.findById(id);
        return new ModelAndView("blog/edit", "blog", blog.get());
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("blog") Blog b) {
        blogService.save(b);
        return "redirect:/";
    }

}
