using Microsoft.AspNetCore.Mvc;
using WebServer.Models;

namespace MovieApi.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class MoviesController : ControllerBase
    {
        private static readonly List<Movie> Movies = new()
        {
            new Movie
            {
                Id = 1,
                Title = "The Shawshank Redemption",
                Director = "Frank Darabont",
                Year = 1994,
                Description = "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
                ImageUrl = "http://10.0.2.2:8080/images/shawshank.jpg",
                Url = "https://www.imdb.com/title/tt0111161/"
            },
            new Movie
            {
                Id = 2,
                Title = "The Godfather",
                Director = "Francis Ford Coppola",
                Year = 1972,
                Description = "The aging patriarch of an organized crime dynasty transfers control of his clan to his reluctant son.",
                ImageUrl = "http://10.0.2.2:8080/images/godfather.jpg",
                Url = "https://www.imdb.com/title/tt0068646/"
            },
            new Movie
            {
                Id = 3,
                Title = "Inception",
                Director = "Christopher Nolan",
                Year = 2010,
                Description = "A mind-bending thriller about dreams within dreams.",
                ImageUrl = "http://10.0.2.2:8080/images/inception.jpg",
                Url = "https://www.imdb.com/title/tt1375666/"
            }
        };

        [HttpGet]
        public ActionResult<IEnumerable<Movie>> GetAllMovies()
        {
            return Ok(Movies);
        }

        [HttpGet("{id}")]
        public ActionResult<Movie> GetMovieById(int id)
        {
            var movie = Movies.FirstOrDefault(m => m.Id == id);
            if (movie == null)
                return NotFound();
            return Ok(movie);
        }
    }
}