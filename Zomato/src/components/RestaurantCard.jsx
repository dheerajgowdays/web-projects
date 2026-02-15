function RestaurantCard({ restaurant }) {
  return (
    <div className="bg-white rounded-xl shadow-md overflow-hidden hover:shadow-xl transition">
      <img
        src={restaurant.image}
        alt={restaurant.name}
        className="h-48 w-full object-cover"
      />

      <div className="p-4">
        <h2 className="text-xl font-semibold">{restaurant.name}</h2>

        <div className="flex justify-between text-sm text-gray-600 mt-2">
          <span>⭐ {restaurant.rating}</span>
          <span>{restaurant.time}</span>
        </div>

        <p className="text-gray-500 text-sm mt-2">
          {restaurant.cuisine}
        </p>
      </div>
    </div>
  )
}

export default RestaurantCard
