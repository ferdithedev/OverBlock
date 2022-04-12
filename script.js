document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();

        document.querySelector(this.getAttribute('href')).scrollIntoView({
            behavior: 'smooth'
        });
    });
});

window.onclick = e => {
    if (e.target.tagName === 'CODE') {
        navigator.clipboard.writeText(e.target.innerText)
        document.getElementsByClassName("info")[0].style.opacity = 1
        setTimeout(
            function () {
                document.getElementsByClassName("info")[0].style.opacity = 0
            }, 2000);
    }
} 