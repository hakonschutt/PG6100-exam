export const formatMovieListToOptions = movies => {
	return movies.map(movie => ({ value: movie.id, label: movie.title }));
};
