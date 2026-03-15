import { create } from 'zustand';
import { persist } from 'zustand/middleware';

export const useCartStore = create(
  persist(
    (set, get) => ({
      items: [],
      restaurantId: null,
      restaurantName: '',

      getTotalItems: () => get().items.reduce((s, i) => s + i.quantity, 0),
      getTotalPrice: () => get().items.reduce((s, i) => s + i.menuItem.price * i.quantity, 0),

      addItem: (menuItem, restaurantId, restaurantName) => {
        const { items, restaurantId: currentId } = get();
        if (currentId && currentId !== restaurantId) {
          set({ items: [], restaurantId: null }); // clear cart from different restaurant
        }
        const existing = items.find(i => i.menuItem.id === menuItem.id);
        set({
          restaurantId,
          restaurantName,
          items: existing
            ? items.map(i => i.menuItem.id === menuItem.id ? { ...i, quantity: i.quantity + 1 } : i)
            : [...items, { menuItem, quantity: 1 }],
        });
      },

      removeItem: (menuItemId) => {
        const { items } = get();
        const item = items.find(i => i.menuItem.id === menuItemId);
        set({
          items: item?.quantity === 1
            ? items.filter(i => i.menuItem.id !== menuItemId)
            : items.map(i => i.menuItem.id === menuItemId ? { ...i, quantity: i.quantity - 1 } : i)
        });
      },

      clearCart: () => set({ items: [], restaurantId: null, restaurantName: '' }),
    }),
    { name: 'zomato-cart' }
  )
);