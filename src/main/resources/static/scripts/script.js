document.addEventListener("DOMContentLoaded", function() {
  // ... (Код для кнопки Fetch Data)

  const homeLink = document.querySelector('.navbar-brand');
  const secondPageLink = document.querySelector('.nav-link[href="/second-page"]');

  homeLink.addEventListener("click", function(event) {
    event.preventDefault();
    navigateToPage("/");
  });

  secondPageLink.addEventListener("click", function(event) {
    event.preventDefault();
    navigateToPage("/second-page");
  });

  function navigateToPage(url) {
    fetch(url)
      .then(response => response.text())
      .then(html => {
        document.querySelector('.container').innerHTML = html;
      })
      .catch(error => {
        console.error("Error navigating:", error);
      });
  }
});
