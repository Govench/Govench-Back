package com.upao.govench.govench.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "community")
public class Community {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "com_id_in", nullable = false)
    private int id;
    @Column(name = "com_nom_vc", nullable = false)
    private String name;
    @Column(name = "com_des_vc")
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "com_own_vc", nullable = false, referencedColumnName = "user_id")
    private User owner;

    @ElementCollection
    @CollectionTable(name = "community_tags", joinColumns = @JoinColumn(name = "com_id_in"))
    @Column(name = "com_tag_vc")
    private List<String> tags;

    @JsonIgnore
    @OneToMany(mappedBy = "comunidad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> post;

    @JsonIgnore
    @OneToMany(mappedBy = "community", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserCommunity> userCommunities;
}
