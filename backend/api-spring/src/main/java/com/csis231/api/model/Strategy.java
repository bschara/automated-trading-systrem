package com.csis231.api.model;

import jakarta.persistence.*;

@Table(name = "trading_strategy")
@Entity
public class Strategy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long strategy_id;
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "indicators", nullable = false)
    private String indicators;

    @Column(name = "order_type")
    private String order_type;

    @Column(name = "entry_rules")
    private String entry_rules;

    @Column(name = "exit_rules")
    private String exit_rules;




    public Strategy() {

    }

    public Strategy(String name, String indicators, String order_type, String entry_rules,
                  String exit_rules) {
        super();
        this.name = name;
        this.order_type = order_type;
        this.indicators = indicators;
        this.entry_rules = entry_rules;
        this.exit_rules = exit_rules;

    }
    public long getId() {
        return strategy_id;
    }
    public void setId(long id) {
        this.strategy_id = id;
    }

    public String getName() {
        return name;
    }

    public void setNAme(String name) {
        this.name = name;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getIndicators() {
        return indicators;
    }

    public void setIndicators(String indicators) {
        this.indicators = indicators;
    }

    public String getEntry_rules() {
        return entry_rules;
    }

    public void setEntry_rules(String entry_rules) {
        this.entry_rules = entry_rules;
    }

    public String getExit_rules() {
        return exit_rules;
    }

    public void setExit_rules(String entry_rules) {
        this.exit_rules = exit_rules;
    }

}