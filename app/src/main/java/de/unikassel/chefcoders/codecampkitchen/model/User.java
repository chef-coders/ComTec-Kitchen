package de.unikassel.chefcoders.codecampkitchen.model;

import java.beans.PropertyChangeSupport;

import java.beans.PropertyChangeListener;

public class User  
{

   public static final String PROPERTY__id = "_id";

   private String _id;

   public String get_id()
   {
      return _id;
   }

   public User set_id(String value)
   {
      if (value == null ? this._id != null : ! value.equals(this._id))
      {
         String oldValue = this._id;
         this._id = value;
         firePropertyChange("_id", oldValue, value);
      }
      return this;
   }


   public static final String PROPERTY_credit = "credit";

   private long credit;

   public long getCredit()
   {
      return credit;
   }

   public User setCredit(long value)
   {
      if (value != this.credit)
      {
         long oldValue = this.credit;
         this.credit = value;
         firePropertyChange("credit", oldValue, value);
      }
      return this;
   }


   public static final String PROPERTY_role = "role";

   private String role;

   public String getRole()
   {
      return role;
   }

   public User setRole(String value)
   {
      if (value == null ? this.role != null : ! value.equals(this.role))
      {
         String oldValue = this.role;
         this.role = value;
         firePropertyChange("role", oldValue, value);
      }
      return this;
   }


   public static final String PROPERTY_created = "created";

   private String created;

   public String getCreated()
   {
      return created;
   }

   public User setCreated(String value)
   {
      if (value == null ? this.created != null : ! value.equals(this.created))
      {
         String oldValue = this.created;
         this.created = value;
         firePropertyChange("created", oldValue, value);
      }
      return this;
   }


   public static final String PROPERTY_name = "name";

   private String name;

   public String getName()
   {
      return name;
   }

   public User setName(String value)
   {
      if (value == null ? this.name != null : ! value.equals(this.name))
      {
         String oldValue = this.name;
         this.name = value;
         firePropertyChange("name", oldValue, value);
      }
      return this;
   }


   public static final String PROPERTY_mail = "mail";

   private String mail;

   public String getMail()
   {
      return mail;
   }

   public User setMail(String value)
   {
      if (value == null ? this.mail != null : ! value.equals(this.mail))
      {
         String oldValue = this.mail;
         this.mail = value;
         firePropertyChange("mail", oldValue, value);
      }
      return this;
   }


   public static final java.util.ArrayList<Purchase> EMPTY_purchases = new java.util.ArrayList<Purchase>()
   { @Override public boolean add(Purchase value){ throw new UnsupportedOperationException("No direct add! Use xy.withPurchases(obj)"); }};


   public static final String PROPERTY_purchases = "purchases";

   private java.util.ArrayList<Purchase> purchases = null;

   public java.util.ArrayList<Purchase> getPurchases()
   {
      if (this.purchases == null)
      {
         return EMPTY_purchases;
      }

      return this.purchases;
   }

   public User withPurchases(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withPurchases(i);
            }
         }
         else if (item instanceof Purchase)
         {
            if (this.purchases == null)
            {
               this.purchases = new java.util.ArrayList<Purchase>();
            }
            if ( ! this.purchases.contains(item))
            {
               this.purchases.add((Purchase)item);
               ((Purchase)item).setUser(this);
               firePropertyChange("purchases", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }



   public User withoutPurchases(Object... value)
   {
      if (this.purchases == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutPurchases(i);
            }
         }
         else if (item instanceof Purchase)
         {
            if (this.purchases.contains(item))
            {
               this.purchases.remove((Purchase)item);
               ((Purchase)item).setUser(null);
               firePropertyChange("purchases", item, null);
            }
         }
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
      result.append(" ").append(this.getName());
      result.append(" ").append(this.getMail());
      result.append(" ").append(this.getRole());


      return result.substring(1);
   }

   public void removeYou()
   {
      this.withoutPurchases(this.getPurchases().clone());


   }


}