package de.unikassel.chefcoders.codecampkitchen.model;

import java.beans.PropertyChangeSupport;

import java.beans.PropertyChangeListener;

public class Purchase  
{

   public static final String PROPERTY__id = "_id";

   private String _id;

   public String get_id()
   {
      return _id;
   }

   public Purchase set_id(String value)
   {
      if (value == null ? this._id != null : ! value.equals(this._id))
      {
         String oldValue = this._id;
         this._id = value;
         firePropertyChange("_id", oldValue, value);
      }
      return this;
   }


   public static final String PROPERTY_created = "created";

   private String created;

   public String getCreated()
   {
      return created;
   }

   public Purchase setCreated(String value)
   {
      if (value == null ? this.created != null : ! value.equals(this.created))
      {
         String oldValue = this.created;
         this.created = value;
         firePropertyChange("created", oldValue, value);
      }
      return this;
   }


   public static final String PROPERTY_amount = "amount";

   private int amount;

   public int getAmount()
   {
      return amount;
   }

   public Purchase setAmount(int value)
   {
      if (value != this.amount)
      {
         int oldValue = this.amount;
         this.amount = value;
         firePropertyChange("amount", oldValue, value);
      }
      return this;
   }


   public static final String PROPERTY_item = "item";

   private Item item;

   public Item getItem()
   {
      return item;
   }

   public Purchase setItem(Item value)
   {
      if (value != this.item)
      {
         Item oldValue = this.item;
         this.item = value;
         firePropertyChange("item", oldValue, value);
      }
      return this;
   }


   public static final String PROPERTY_user = "user";

   private User user = null;

   public User getUser()
   {
      return this.user;
   }

   public Purchase setUser(User value)
   {
      if (this.user != value)
      {
         User oldValue = this.user;
         if (this.user != null)
         {
            this.user = null;
            oldValue.withoutPurchases(this);
         }
         this.user = value;
         if (value != null)
         {
            value.withPurchases(this);
         }
         firePropertyChange("user", oldValue, value);
      }
      return this;
   }



   protected PropertyChangeSupport listeners = null;

   public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue)
   {
      if (listeners != null)
      {
         listeners.firePropertyChange(propertyName, oldValue, newValue);
         return true;
      }
      return false;
   }

   public boolean addPropertyChangeListener(PropertyChangeListener listener)
   {
      if (listeners == null)
      {
         listeners = new PropertyChangeSupport(this);
      }
      listeners.addPropertyChangeListener(listener);
      return true;
   }

   public boolean addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
   {
      if (listeners == null)
      {
         listeners = new PropertyChangeSupport(this);
      }
      listeners.addPropertyChangeListener(propertyName, listener);
      return true;
   }

   public boolean removePropertyChangeListener(PropertyChangeListener listener)
   {
      if (listeners != null)
      {
         listeners.removePropertyChangeListener(listener);
      }
      return true;
   }

   public boolean removePropertyChangeListener(String propertyName,PropertyChangeListener listener)
   {
      if (listeners != null)
      {
         listeners.removePropertyChangeListener(propertyName, listener);
      }
      return true;
   }

   @Override
   public String toString()
   {
      StringBuilder result = new StringBuilder();

      result.append(" ").append(this.get_id());
      result.append(" ").append(this.getCreated());
      result.append(" ").append(this.getUser_id());
      result.append(" ").append(this.getItem_id());


      return result.substring(1);
   }

   public void removeYou()
   {
      this.setUser(null);

   }


   public static final String PROPERTY_user_id = "user_id";

   private String user_id;

   public String getUser_id()
   {
      return user_id;
   }

   public Purchase setUser_id(String value)
   {
      if (value == null ? this.user_id != null : ! value.equals(this.user_id))
      {
         String oldValue = this.user_id;
         this.user_id = value;
         firePropertyChange("user_id", oldValue, value);
      }
      return this;
   }


   public static final String PROPERTY_item_id = "item_id";

   private String item_id;

   public String getItem_id()
   {
      return item_id;
   }

   public Purchase setItem_id(String value)
   {
      if (value == null ? this.item_id != null : ! value.equals(this.item_id))
      {
         String oldValue = this.item_id;
         this.item_id = value;
         firePropertyChange("item_id", oldValue, value);
      }
      return this;
   }


}