import { create } from "zustand";
import { persist } from "zustand/middleware";

export const useCartStore = create(
  persist(
    (set,get) =>({
      items : [],
      restaurantId: null,
      restaurantName: '',

      getTotalItems: () => get().items.reduce((s,i) => s + i.quantity,0),
      getTotalPrice: () => get().items.reduce((s,i) => s + i.menuItem.price * i.quantity,0),

      addItem: (menuItem , restaurantId , restaurantName) =>{
        const { items, restaurantId: currentId } = get();
        if( currentId && currentId !== restaurantId){
          set({items:[],restaurantId: null});
        }
        const existing = items.find(i => i.menuItem.id === menuItem.id);
        set({
          restaurantId,
          restaurantName,
          items: existing ? items.map(function(i){
            if(i.menuItem.id === menuItem.id){
              return{
                menuItem: i.menuItem,
                quantity: i.quantity + 1
              };
            }
            else{
              return i;
            }
          })
          : items.concat([
            {
              menuItem: menuItem,
              quantity: 1
            }
          ])
        });
      },
      removeItem: (menuItem) => {
        const { items } = get();
        const items = items.find(i => i.menuItem.id === menuItemId);
        
      },

      clearCart: () => set({ items:[] , restaurantId : null , restaurantName: ''}),
    }),
    {name: 'zomato-cart' }
  )
);