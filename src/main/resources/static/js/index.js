window.addEventListener("DOMContentLoaded", () => {

    $('.carousel').slick({
        autoplay: true,
        autoplaySpeed: 3000,
        prevArrow: '<div class="carousel_arrow carousel_prev"><i class="fas fa-chevron-left"></i></div>',
        nextArrow: '<div class="carousel_arrow carousel_next"><i class="fas fa-chevron-right"></i></div>'
    });
});